package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.world.level.Level
import software.bernie.geckolib.animation.AnimatableManager

class ToySoldierGunnerEntity(level: Level) : ToySoldierEntity(ModEntityTypes.TOY_SOLDIER_GUNNER.get(), level) {

	override val requiredWhimsy: Double = 10.0
	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {}

}