package dev.aaronhowser.mods.paracosm.entity

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
import software.bernie.geckolib.animation.*

class AaronberryEntity : ToyEntity {

	constructor(
		entityType: EntityType<AaronberryEntity>,
		level: Level
	) : super(entityType, level)

	override val requiredWhimsy: Double = 1.0

	override fun registerGoals() {
		goalSelector.addGoal(0, FloatGoal(this))
		goalSelector.addGoal(2, SitWhenOrderedToGoal(this))
		goalSelector.addGoal(3, ToyStrollGoal(this, 1.0))
		goalSelector.addGoal(4, ToyLookAtPlayerGoal(this))
		goalSelector.addGoal(5, ToyRandomLookAroundGoal(this))
	}

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(AnimationController(this, "controller", 0, this::animationPredicate))
	}

	private fun animationPredicate(animationState: AnimationState<AaronberryEntity>): PlayState {
		val animationName = when {
			isHiding || isInSittingPose -> "animation.aaronberry.sit"
			animationState.isMoving -> "animation.aaronberry.walk"
			else -> return PlayState.STOP
		}

		animationState.controller.setAnimation(
			RawAnimation.begin().then(
				animationName,
				Animation.LoopType.LOOP
			)
		)

		return PlayState.CONTINUE
	}

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