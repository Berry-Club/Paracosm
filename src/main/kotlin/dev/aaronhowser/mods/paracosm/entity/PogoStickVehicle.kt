package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.status
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.entity.base.IUpgradeableEntity
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import dev.aaronhowser.mods.paracosm.packet.client_to_server.UpdatePogoControls
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
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

	constructor(
		level: Level,
		spawnLocation: Vec3
	) : this(ModEntityTypes.POGO_STICK_VEHICLE.get(), level) {
		this.setPos(spawnLocation)
	}

	data class Controls(
		var leftImpulse: Float = 0f,
		var forwardImpulse: Float = 0f,
		var spaceHeld: Boolean = false
	)

	val controls = Controls()
	private var lastSentControls = Controls()
	private var controlPacketCooldown = 0

	var tiltRight: Float
		get() = entityData.get(DATA_TILT_RIGHT)
		private set(value) = entityData.set(DATA_TILT_RIGHT, value)

	var tiltBackward: Float
		get() = entityData.get(DATA_TILT_BACKWARD)
		private set(value) = entityData.set(DATA_TILT_BACKWARD, value)

	var jumpPercent: Float
		get() = entityData.get(DATA_JUMP_PERCENT)
		private set(value) = entityData.set(DATA_JUMP_PERCENT, value)

	var previousTiltRight: Float = 0f
	var previousTiltBackward: Float = 0f
	var previousJumpPercent: Float = 0f

	init {
		blocksBuilding = true
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		super.defineSynchedData(builder)
		builder.define(DATA_TILT_RIGHT, 0f)
		builder.define(DATA_TILT_BACKWARD, 0f)
		builder.define(DATA_JUMP_PERCENT, 0f)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		IUpgradeableEntity.loadUpgrades(this, compound)
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		IUpgradeableEntity.saveUpgrades(this, compound)
	}

	override fun getDropItem(): Item = ModItems.POGO_STICK.get()

	override fun destroy(dropItem: Item) {
		kill()

		if (level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			spawnAtLocation(getStack())
		}
	}

	override fun canCollideWith(entity: Entity): Boolean =
		hasControllingPassenger()
				&& entity.y + (entity.bbHeight * 0.75) < y
				&& Boat.canVehicleCollide(this, entity)

	override fun isPushable(): Boolean = !hasControllingPassenger()

	override fun isPickable(): Boolean = !isRemoved

	override fun getPickResult(): ItemStack? = if (!isPickable) null else getStack()

	private fun getStack(): ItemStack {
		val stack = ItemStack(dropItem)
		stack.set(DataComponents.CUSTOM_NAME, customName)
		for (upgrade in IUpgradeableEntity.getUpgrades(this)) {
			IUpgradeableItem.addUpgrade(stack, upgrade)
		}
		return stack
	}

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(
			AnimationController(this, "controller", 0) { PlayState.STOP }
		)
	}

	private val cache = SingletonAnimatableInstanceCache(this)
	override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

	override fun interact(player: Player, hand: InteractionHand): InteractionResult {
		val defaultResult = super.interact(player, hand)
		if (defaultResult.consumesAction()) return defaultResult

		if (player.isSecondaryUseActive) return InteractionResult.PASS
		if (isVehicle) return InteractionResult.PASS

		return if (isClientSide) InteractionResult.SUCCESS
		else if (player.startRiding(this)) InteractionResult.CONSUME else InteractionResult.PASS
	}

	override fun getControllingPassenger(): LivingEntity? = passengers.firstOrNull() as? LivingEntity

	fun setInput(leftImpulse: Float, forwardImpulse: Float, jumping: Boolean) {
		controls.leftImpulse = leftImpulse
		controls.forwardImpulse = forwardImpulse
		controls.spaceHeld = jumping

		if (isClientSide) {
			if (controlPacketCooldown <= 0 && controlsChangedSignificantly()) {
				UpdatePogoControls(leftImpulse, forwardImpulse, jumping).messageServer()
				lastSentControls.leftImpulse = leftImpulse
				lastSentControls.forwardImpulse = forwardImpulse
				lastSentControls.spaceHeld = jumping
				controlPacketCooldown = 2
			}
		}
	}

	private fun controlsChangedSignificantly(): Boolean {
		val differenceLeft = abs(controls.leftImpulse - lastSentControls.leftImpulse)
		val differenceForward = abs(controls.forwardImpulse - lastSentControls.forwardImpulse)
		val differenceSpace = controls.spaceHeld != lastSentControls.spaceHeld
		return differenceLeft > 1e-3f || differenceForward > 1e-3f || differenceSpace
	}

	override fun tick() {
		super.tick()

		if (controlPacketCooldown > 0) controlPacketCooldown--

		doMove()
		tryJump()
		updateTilt()
		updateMomentum()
		tryResetControls()
	}

	override fun getDefaultGravity(): Double = 0.04

	override fun getGravity(): Double {
		return when {
			isNoGravity -> 0.0
			IUpgradeableEntity.hasUpgrade(this, PogoStickItem.Upgrades.LOWER_GRAVITY) -> defaultGravity / 2
			else -> defaultGravity
		}
	}

	private fun doMove() {
		applyGravity()
		move(MoverType.SELF, deltaMovement)
		if (onGround()) {
			deltaMovement = Vec3.ZERO
		}
	}

	private fun tryResetControls() {
		if (hasControllingPassenger()) return

		controls.leftImpulse = 0f
		controls.forwardImpulse = 0f
		controls.spaceHeld = false

		lastSentControls.leftImpulse = 0f
		lastSentControls.forwardImpulse = 0f
		lastSentControls.spaceHeld = false
	}

	private var ticksOnGround = 0
	private var bounceForce = 0f
	private val maxTicksOnGround = 20 * 4

	private fun updateMomentum() {
		if (onGround()) {
			ticksOnGround++
			if (ticksOnGround > maxTicksOnGround) bounceForce = 0f
		} else {
			ticksOnGround = 0
		}

		if (fallDistance > 0f) bounceForce = fallDistance + 0.69f

		val rider = controllingPassenger
		if (rider is Player && rider.isClientSide) {
			val momentumString = String.format("%.2f", bounceForce)
			rider.status(Component.literal("Bounce force: $momentumString"))
		}
	}

	private fun getJumpStrengthForDistance(distance: Number): Double {
		return sqrt(2 * gravity * distance.toDouble())
	}

	private fun tryJump() {
		if (controls.spaceHeld) return

		val currentJumpAmount = jumpPercent
		if (currentJumpAmount <= 0.1f) return

		val canJump = onGround() || IUpgradeableEntity.hasUpgrade(this, PogoStickItem.Upgrades.GEPPO)

		if (canJump) {
			val distance = (bounceForce * 1.5f).toDouble().coerceIn(7.5, 20.0)
			val jumpStrength = getJumpStrengthForDistance(distance)
			val jumpVector = Vec3(0.0, 1.0, 0.0)
				.xRot(tiltBackward * MAX_TILT_RADIAN)
				.zRot(tiltRight * MAX_TILT_RADIAN)
				.yRot(yRot * Mth.DEG_TO_RAD)
				.scale(currentJumpAmount * jumpStrength)

			deltaMovement = jumpVector
			hasImpulse = true
			setOnGround(false)
			bounceForce = 0f
		}

		jumpPercent = 0f
	}

	private fun updateTilt() {
		previousTiltBackward = tiltBackward
		previousTiltRight = tiltRight
		previousJumpPercent = jumpPercent

		val rider = controllingPassenger
		if (rider != null) {
			setOldPosAndRot()
			setRot(-rider.yRot, 0f)
		}

		var currentTiltBackward = tiltBackward
		var currentTiltRight = tiltRight
		var currentJumpAmount = jumpPercent

		if (controls.forwardImpulse > 0f) currentTiltBackward -= 0.1f
		else if (controls.forwardImpulse < 0f) currentTiltBackward += 0.1f
		else {
			currentTiltBackward += if (currentTiltBackward > 0f) -0.01f else 0.01f
			if (currentTiltBackward in -0.09f..0.09f) currentTiltBackward = 0.0f
		}

		if (controls.leftImpulse > 0f) currentTiltRight += 0.1f
		else if (controls.leftImpulse < 0f) currentTiltRight -= 0.1f
		else {
			currentTiltRight += if (currentTiltRight > 0f) -0.05f else 0.05f
			if (currentTiltRight in -0.09f..0.09f) currentTiltRight = 0.0f
		}

		if (controls.spaceHeld) currentJumpAmount += 0.1f

		currentTiltBackward = currentTiltBackward.coerceIn(-1.0f, 1.0f)
		currentTiltRight = currentTiltRight.coerceIn(-1.0f, 1.0f)
		currentJumpAmount = currentJumpAmount.coerceIn(0.0f, 1.0f)

		tiltBackward = currentTiltBackward
		tiltRight = currentTiltRight
		jumpPercent = currentJumpAmount
	}

	override fun shouldRiderSit(): Boolean = false

	override fun getPassengerAttachmentPoint(
		entity: Entity,
		dimensions: EntityDimensions,
		partialTick: Float
	): Vec3 {
		val heightScale = 1.0 - JUMP_ANIM_DISTANCE * jumpPercent
		val (percentBack, percentRight) = OtherUtil.getRotationForCircle(tiltBackward, tiltRight)

		return Vec3(0.0, 1.0, 0.0)
			.xRot(percentBack * MAX_TILT_RADIAN)
			.zRot(percentRight * MAX_TILT_RADIAN)
			.yRot(yRot * Mth.DEG_TO_RAD)
			.scale(heightScale)
	}

	override fun getDismountLocationForPassenger(passenger: LivingEntity): Vec3 = position()

	override fun setOnGroundWithMovement(onGround: Boolean, movement: Vec3) {
		super.setOnGroundWithMovement(onGround, movement)

		if (!onGround) return
		if (movement.y >= -gravity * 5) return
		if (!hasControllingPassenger()) return
		if (!IUpgradeableEntity.hasUpgrade(this, PogoStickItem.Upgrades.GOOMBA_STOMP)) return

		fun shouldStomp(entity: Entity): Boolean {
			if (entity === this) return false
			if (entity === controllingPassenger) return false
			if (entity.y > y) return false
			if (entity is OwnableEntity && entity.owner == controllingPassenger) return false
			return true
		}

		val radius = ServerConfig.CONFIG.pogoGoombaRadius.get()
		val stompedEntities = level().getEntities(
			this,
			AABB(x - radius, y - 0.5, z - radius, x + radius, y + 0.5, z + radius)
		).filter { shouldStomp(it) }

		val damage = abs(movement.y.toFloat())
		for (entity in stompedEntities) {
			entity.hurt(damageSources().fall(), damage)
		}
	}

	companion object {
		private fun floatData(): EntityDataAccessor<Float> =
			SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)

		val DATA_TILT_RIGHT: EntityDataAccessor<Float> = floatData()
		val DATA_TILT_BACKWARD: EntityDataAccessor<Float> = floatData()
		val DATA_JUMP_PERCENT: EntityDataAccessor<Float> = floatData()

		const val JUMP_ANIM_DISTANCE = 0.4
		const val MAX_TILT_DEGREE = 45f
		const val MAX_TILT_RADIAN = MAX_TILT_DEGREE * Mth.DEG_TO_RAD

		fun handleInput(event: ClientTickEvent.Pre) {
			val player = AaronClientUtil.localPlayer as? LocalPlayer ?: return
			val vehicle = player.vehicle as? PogoStickVehicle ?: return

			vehicle.setInput(player.input.leftImpulse, player.input.forwardImpulse, player.input.jumping)
		}

		fun checkCancelDamage(event: LivingIncomingDamageEvent) {
			if (event.isCanceled) return
			val source = event.source
			val entity = event.entity

			if (source != entity.damageSources().fall() && source != entity.damageSources().inWall()) return
			if (event.entity.vehicle is PogoStickVehicle) {
				event.isCanceled = true
			}
		}
	}
}