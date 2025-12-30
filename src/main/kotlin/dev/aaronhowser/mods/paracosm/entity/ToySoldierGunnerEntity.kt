package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.world.entity.EntityType
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
	}

}