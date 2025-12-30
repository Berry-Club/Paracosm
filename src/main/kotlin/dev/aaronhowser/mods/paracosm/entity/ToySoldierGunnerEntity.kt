package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.world.entity.EntityType
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
	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

}