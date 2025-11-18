package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.entity.base.IUpgradeableEntity
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.client_to_server.UpdatePogoControls
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.entity.vehicle.VehicleEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import kotlin.math.abs
import kotlin.math.sqrt

class PogoStickVehicle(
	entityType: EntityType<*>,
	level: Level
) : VehicleEntity(entityType, level), GeoEntity, IUpgradeableEntity {

	//TODO: If you fall from high, bounce to high
	//TODO: Should it bounce even if you didn't tell it to, if it was already bouncing?

	constructor(
		level: Level,
		spawnLocation: Vec3
	) : this(ModEntityTypes.POGO_STICK_VEHICLE.get(), level) {
		this.setPos(spawnLocation)
	}

	init {
		this.blocksBuilding = true
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		super.defineSynchedData(builder)
		builder.define(DATA_TILT_RIGHT, 0f)
		builder.define(DATA_TILT_BACKWARD, 0f)
		builder.define(DATA_JUMP_PERCENT, 0f)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		loadUpgrades(this, compound)
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		saveUpgrades(this, compound)
	}

	override fun getDropItem(): Item {
		return ModItems.POGO_STICK.get()
	}

	override fun destroy(dropItem: Item) {
		this.kill()
		if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			val stack = getStack()

			this.spawnAtLocation(stack)
		}
	}

	override fun canCollideWith(entity: Entity): Boolean {
		return this.hasControllingPassenger()
				&& entity.y + (entity.bbHeight * 0.75) < this.y
				&& Boat.canVehicleCollide(this, entity)
	}

	override fun isPushable(): Boolean {
		return !this.hasControllingPassenger()
	}

	override fun isPickable(): Boolean {
		return !isRemoved
	}

	override fun getPickResult(): ItemStack? {
		if (!isPickable) return null

		return getStack()
	}

	private fun getStack(): ItemStack {
		val stack = ItemStack(this.dropItem)
		stack.set(DataComponents.CUSTOM_NAME, this.customName)

		for (upgrade in Upgradeable.getUpgrades(this)) {
			Upgradeable.addUpgrade(stack, upgrade)
		}

		return stack
	}

	// GeckoLib stuff

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(
			AnimationController(
				this, "controller", 0
			) {
				PlayState.STOP
			}
		)
	}

	private val cache = SingletonAnimatableInstanceCache(this)

	override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
		return cache
	}

	// Ride stuff

	override fun interact(player: Player, hand: InteractionHand): InteractionResult {
		val defaultResult = super.interact(player, hand)
		if (defaultResult.consumesAction()) {
			return defaultResult
		}

		if (player.isSecondaryUseActive) {
			return InteractionResult.PASS
		}

		if (this.isVehicle) {
			return InteractionResult.PASS
		}

		if (this.isServerSide) {
			return if (player.startRiding(this)) {
				InteractionResult.CONSUME
			} else {
				InteractionResult.PASS
			}
		}

		return InteractionResult.SUCCESS
	}

	override fun getControllingPassenger(): LivingEntity? {
		return passengers.firstOrNull() as? LivingEntity
	}

	class Controls(
		var leftImpulse: Float = 0f,
		var forwardImpulse: Float = 0f,
		var spaceHeld: Boolean = false
	)

	val controls = Controls()

	fun setInput(leftImpulse: Float, forwardImpulse: Float, jumping: Boolean) {
		this.controls.leftImpulse = leftImpulse
		this.controls.forwardImpulse = forwardImpulse
		this.controls.spaceHeld = jumping

		if (this.isClientSide) {
			ModPacketHandler.messageServer(
				UpdatePogoControls(
					leftImpulse,
					forwardImpulse,
					jumping
				)
			)
		}
	}

	override fun tick() {
		super.tick()

		tryJump()
		doMove()
		updateTilt()
		updateMomentum()
	}

	override fun getDefaultGravity(): Double {
		return 0.04
	}

	override fun getGravity(): Double {
		return if (this.isNoGravity) {
			0.0
		} else if (Upgradeable.hasUpgrade(this, PogoStickItem.Upgrades.LOWER_GRAVITY)) {
			this.defaultGravity / 2
		} else {
			this.defaultGravity
		}
	}

	private fun doMove() {
		this.applyGravity()
		this.move(MoverType.SELF, this.deltaMovement)

		if (this.onGround()) {
			this.deltaMovement = Vec3.ZERO
		}
	}

	private fun tryResetControls() {
		if (this.hasControllingPassenger()) return

		this.controls.leftImpulse = 0f
		this.controls.forwardImpulse = 0f
		this.controls.spaceHeld = false
	}

	private var ticksOnGround = 0
	private var verticalMomentum = 0f
	private val maxTicksOnGround = 20 * 4

	private fun updateMomentum() {
		if (this.onGround()) {
			this.ticksOnGround++

			if (this.ticksOnGround > this.maxTicksOnGround) {
				this.verticalMomentum = 0f
			}
		} else {
			this.ticksOnGround = 0
		}

		if (this.fallDistance != 0f) {
			this.verticalMomentum =
				this.fallDistance + 0.69f // Nice (For some reason fallDistance skips the first 0.69)
		}

		val rider = this.controllingPassenger
		if (rider is Player && rider.isClientSide) {
			val momentumString = String.format("%.2f", this.verticalMomentum)

			rider.displayClientMessage(
				Component.literal("Stored vertical momentum: $momentumString"),
				true
			)
		}
	}

	private fun getJumpStrengthForDistance(distance: Number): Double {
		return sqrt(2 * this.gravity * distance.toDouble())
	}

	private fun tryJump() {
		if (this.controls.spaceHeld) return

		val currentJumpAmount = this.entityData.get(DATA_JUMP_PERCENT)
		if (currentJumpAmount <= 0.1) return

		if (this.onGround() || Upgradeable.hasUpgrade(this, PogoStickItem.Upgrades.GEPPO)) {
			val currentTiltBack = this.entityData.get(DATA_TILT_BACKWARD)
			val currentTiltRight = this.entityData.get(DATA_TILT_RIGHT)

			val distance = (this.verticalMomentum * 1.5).coerceIn(7.5, 20.0)
			val jumpStrength = getJumpStrengthForDistance(distance)

			val jumpVector =
				// Unit Vector
				Vec3(0.0, 1.0, 0.0)
					// Tilting
					.xRot(currentTiltBack * MAX_TILT_RADIAN)
					.zRot(currentTiltRight * MAX_TILT_RADIAN)
					.yRot(this.yRot * Mth.DEG_TO_RAD)
					// Scaling
					.scale(currentJumpAmount.toDouble())
					.scale(jumpStrength)

			addDeltaMovement(jumpVector)
			this.hasImpulse = true
			this.setOnGround(false)

			this.verticalMomentum = 0f
		}

		this.entityData.set(DATA_JUMP_PERCENT, 0.0f)
	}

	private fun updateTilt() {
		val rider = this.controllingPassenger
		if (rider != null) {
			this.setRot(
				-rider.yRot,
				0f
			)
		}

		var currentTiltBackward = this.entityData.get(DATA_TILT_BACKWARD)
		var currentTiltRight = this.entityData.get(DATA_TILT_RIGHT)
		var currentJumpAmount = this.entityData.get(DATA_JUMP_PERCENT)

		if (this.controls.forwardImpulse > 0) {
			currentTiltBackward -= 0.1f
		} else if (this.controls.forwardImpulse < 0) {
			currentTiltBackward += 0.1f
		} else {
			currentTiltBackward += if (currentTiltBackward > 0) -0.01f else 0.01f
			if (currentTiltBackward in -0.09..0.09) {
				currentTiltBackward = 0.0f
			}
		}

		if (this.controls.leftImpulse > 0) {
			currentTiltRight += 0.1f
		} else if (this.controls.leftImpulse < 0) {
			currentTiltRight -= 0.1f
		} else {
			currentTiltRight += if (currentTiltRight > 0) -0.05f else 0.05f
			if (currentTiltRight in -0.09..0.09) {
				currentTiltRight = 0.0f
			}
		}

		if (this.controls.spaceHeld) {
			currentJumpAmount += 0.1f
		}

		currentTiltBackward = currentTiltBackward.coerceIn(-1.0f, 1.0f)
		currentTiltRight = currentTiltRight.coerceIn(-1.0f, 1.0f)
		currentJumpAmount = currentJumpAmount.coerceIn(0.0f, 1.0f)

		this.entityData.set(DATA_TILT_BACKWARD, currentTiltBackward)
		this.entityData.set(DATA_TILT_RIGHT, currentTiltRight)
		this.entityData.set(DATA_JUMP_PERCENT, currentJumpAmount)

		tryResetControls()
	}

	override fun shouldRiderSit(): Boolean {
		return false
	}

	override fun getPassengerAttachmentPoint(
		entity: Entity,
		dimensions: EntityDimensions,
		partialTick: Float
	): Vec3 {
		val height = 1 - JUMP_ANIM_DISTANCE * this.entityData.get(DATA_JUMP_PERCENT).toDouble()

		val tiltPair = OtherUtil.getRotationForCircle(
			this.entityData.get(DATA_TILT_BACKWARD),
			this.entityData.get(DATA_TILT_RIGHT)
		)

		val tiltBackward = tiltPair.backwards
		val tiltRight = tiltPair.right

		return Vec3(0.0, 1.0, 0.0)
			.xRot(tiltBackward * MAX_TILT_RADIAN)
			.zRot(tiltRight * MAX_TILT_RADIAN)
			.yRot(this.yRot * Mth.DEG_TO_RAD)
			.scale(height)
	}

	override fun getDismountLocationForPassenger(passenger: LivingEntity): Vec3 {
		return this.position()
	}

	override fun setOnGroundWithMovement(onGround: Boolean, movement: Vec3) {
		super.setOnGroundWithMovement(onGround, movement)

		if (
			onGround
			&& movement.y < -this.gravity * 5
			&& this.hasControllingPassenger()
			&& Upgradeable.hasUpgrade(this, PogoStickItem.Upgrades.GOOMBA_STOMP)
		) {

			fun shouldStomp(entity: Entity): Boolean {
				if (entity == this) return false
				if (entity == this.controllingPassenger) return false
				if (entity.y > this.y) return false
				if (entity is OwnableEntity && entity.owner == this.controllingPassenger) return false

				return true
			}

			val radius = ServerConfig.CONFIG.pogoGoombaRadius.get()

			val stompedEntities = this.level().getEntities(
				this,
				AABB(
					this.x - radius,
					this.y - 0.5,
					this.z - radius,
					this.x + radius,
					this.y + 0.5,
					this.z + radius
				)
			).filter { shouldStomp(it) }

			//TODO: Improve this
			val damage = abs(movement.y.toFloat())

			for (entity in stompedEntities) {
				//TODO: Add a new damage source
				entity.hurt(this.level().damageSources().fall(), damage)
			}
		}
	}

	companion object {
		val DATA_TILT_RIGHT: EntityDataAccessor<Float> =
			SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
		val DATA_TILT_BACKWARD: EntityDataAccessor<Float> =
			SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
		val DATA_JUMP_PERCENT: EntityDataAccessor<Float> =
			SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)

		const val JUMP_ANIM_DISTANCE = 0.4

		const val MAX_TILT_DEGREE = 45f
		const val MAX_TILT_RADIAN = MAX_TILT_DEGREE * Mth.DEG_TO_RAD

		fun handleInput(event: ClientTickEvent.Pre) {
			val player = AaronClientUtil.localPlayer as? LocalPlayer ?: return
			val vehicle = player.vehicle as? PogoStickVehicle ?: return

			vehicle.setInput(
				player.input.leftImpulse,
				player.input.forwardImpulse,
				player.input.jumping
			)
		}

		fun checkCancelDamage(event: LivingIncomingDamageEvent) {
			if (event.isCanceled) return

			val source = event.source
			val entity = event.entity
			val level = entity.level()

			if (
				source != level.damageSources().fall()
				&& source != level.damageSources().inWall()
			) return

			if (event.entity.vehicle is PogoStickVehicle) {
				event.isCanceled = true
			}
		}
	}


}