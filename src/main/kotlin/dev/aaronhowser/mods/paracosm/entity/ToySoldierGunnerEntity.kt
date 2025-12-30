package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isMoving
import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyStrollGoal
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.level.Level
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation

class ToySoldierGunnerEntity : ToySoldierEntity {

	constructor(
		level: Level
	) : this(ModEntityTypes.TOY_SOLDIER_GUNNER.get(), level)

	constructor(
		entityType: EntityType<ToySoldierGunnerEntity>,
		level: Level
	) : super(entityType, level)

	override val requiredWhimsy: Double = 10.0

	override fun registerGoals() {
		goalSelector.addGoal(0, FloatGoal(this))
		goalSelector.addGoal(2, SitWhenOrderedToGoal(this))
		goalSelector.addGoal(3, ToyStrollGoal(this, 1.0))
		goalSelector.addGoal(4, ToyLookAtPlayerGoal(this))
		goalSelector.addGoal(5, ToyRandomLookAroundGoal(this))
	}

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		val walking = AnimationController(this, "walking", 0) {
			return@AnimationController if (it.isMoving) {
				it.setAndContinue(WALK_ANIM)
			} else {
				PlayState.STOP
			}
		}

		controllers.add(walking)
	}

	companion object {
		val WALK_ANIM: RawAnimation = RawAnimation.begin().thenLoop("animation.toygunner.walk")

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