package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyStrollGoal
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PlayerRideableJumping
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.*

class StringWormEntity(
	entityType: EntityType<StringWormEntity>,
	level: Level
) : ToyEntity(entityType, level), PlayerRideableJumping {

	// Entity setup

	override val requiredWhimsy: Double = 5.0

	override fun registerGoals() {
		this.goalSelector.let {
			it.addGoal(0, FloatGoal(this))
			it.addGoal(2, SitWhenOrderedToGoal(this))
			it.addGoal(3, ToyStrollGoal(this, 1.0))
			it.addGoal(4, ToyLookAtPlayerGoal(this))
			it.addGoal(5, ToyRandomLookAroundGoal(this))
		}
	}

	// Animation

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(AnimationController(this, "controller", 0, this::predicate))
	}

	private fun predicate(animationState: AnimationState<StringWormEntity>): PlayState {
		val animationName = if (this.isHiding) {
			return PlayState.STOP
		} else if (animationState.isMoving) {
			"animation.stringworm.slither"
		} else {
			return PlayState.STOP
		}

		animationState.controller.setAnimation(
			RawAnimation.begin().then(
				animationName,
				Animation.LoopType.LOOP
			)
		)

		return PlayState.CONTINUE
	}

	// Behavior

	override fun isFood(p0: ItemStack): Boolean {
		return p0.item == ModItems.COTTON.get()
	}


	private fun fedFood(usedStack: ItemStack): InteractionResult {
		if (!this.isFood(usedStack)) return InteractionResult.FAIL

		if (this.health < this.maxHealth) {
			val healAmount = 1f
			this.heal(healAmount)

			return InteractionResult.sidedSuccess(this.isClientSide)
		}

		if (this.scale < 2.0) {
			this.getAttribute(Attributes.SCALE)?.let {
				it.baseValue += 0.1
			}

			this.refreshDimensions()

			return InteractionResult.sidedSuccess(this.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
		if (this.isVehicle || player.isSecondaryUseActive) return super.mobInteract(player, hand)

		val usedStack = player.getItemInHand(hand)

		if (!usedStack.isEmpty) {
			if (this.isFood(usedStack)) return this.fedFood(usedStack)

			val itemUseResult = usedStack.interactLivingEntity(player, this, hand)
			if (itemUseResult.consumesAction()) return itemUseResult
		}

		this.doPlayerRide(player)
		return InteractionResult.sidedSuccess(this.isClientSide)
	}

	// Riding

	private fun doPlayerRide(player: Player) {
		if (this.isClientSide) return

		player.yRot = this.yRot
		player.xRot = this.xRot
		player.startRiding(this)
	}

	override fun isTame(): Boolean {
		return true
	}

	override fun getControllingPassenger(): LivingEntity? {
		return passengers.firstOrNull() as? LivingEntity
	}

	override fun getRiddenInput(player: Player, travelVector: Vec3): Vec3 {
		val leftRight = player.xxa * 0.5f
		var forwardBackward = player.zza
		if (forwardBackward <= 0.0f) {
			forwardBackward *= 0.25f
		}

		return Vec3(leftRight.toDouble(), 0.0, forwardBackward.toDouble())
	}

	override fun getRiddenSpeed(player: Player): Float {
		return (getAttributeValue(Attributes.MOVEMENT_SPEED) * getAttributeValue(Attributes.SCALE)).toFloat()
	}

	override fun tickRidden(player: Player, travelVector: Vec3) {
		super.tickRidden(player, travelVector)

		this.setRot(
			player.yRot,
			(player.xRot * 0.5f).coerceIn(-25f..0f)
		)

		this.yRotO = this.yRot
		this.yBodyRot = this.yRot
		this.yHeadRot = this.yRot
	}

	private var playerJumpPendingScale = 0f
	override fun onPlayerJump(jumpPower: Int) {
		val power = jumpPower.coerceAtLeast(0)

		playerJumpPendingScale = if (jumpPower > 90) {
			1f
		} else {
			0.4f + (0.4f * power / 90f)
		}
	}

	override fun canJump(): Boolean {
		return true
	}

	override fun handleStartJump(p0: Int) {
	}

	override fun handleStopJump() {
	}

	companion object {
		fun setAttributes(): AttributeSupplier {
			return createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.ATTACK_DAMAGE, 2.0)
				.add(Attributes.ATTACK_SPEED, 1.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2)
				.add(Attributes.SCALE, 0.1)
				.build()
		}
	}

}