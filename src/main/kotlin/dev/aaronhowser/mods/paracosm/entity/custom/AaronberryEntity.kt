package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyStrollGoal
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.*

class AaronberryEntity(
	entityType: EntityType<AaronberryEntity>,
	level: Level
) : ToyEntity(entityType, level) {

	override val requiredWhimsy: Float = 1f

	override fun registerGoals() {
		this.goalSelector.let {
			it.addGoal(0, FloatGoal(this))
			it.addGoal(2, SitWhenOrderedToGoal(this))
			it.addGoal(3, ToyStrollGoal(this, 1.0))
			it.addGoal(4, ToyLookAtPlayerGoal(this))
			it.addGoal(5, ToyRandomLookAroundGoal(this))
		}
	}

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(AnimationController(this, "controller", 0, this::predicate))
	}

	private fun predicate(animationState: AnimationState<AaronberryEntity>): PlayState {
		val animationName = if (isHiding || isInSittingPose) {
			"animation.aaronberry.sit"
		} else if (animationState.isMoving) {
			"animation.aaronberry.walk"
		} else return PlayState.STOP

		animationState.controller.setAnimation(
			RawAnimation.begin().then(
				animationName,
				Animation.LoopType.LOOP
			)
		)

		return PlayState.CONTINUE
	}

	private val cache = SingletonAnimatableInstanceCache(this)
	override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

	companion object {
		fun setAttributes(): AttributeSupplier {
			return createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.ATTACK_DAMAGE, 2.0)
				.add(Attributes.ATTACK_SPEED, 1.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2)
				.build()
		}
	}

}