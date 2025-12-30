package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import software.bernie.geckolib.animation.AnimatableManager

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
		goalSelector.addGoal(4, ToyLookAtPlayerGoal(this))
		goalSelector.addGoal(5, ToyRandomLookAroundGoal(this))
	}

	override fun tick() {
		super.tick()


	}

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

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