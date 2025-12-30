package dev.aaronhowser.mods.paracosm.entity.base

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.level.Level

abstract class ToySoldierEntity(
	entityType: EntityType<out ToySoldierEntity>,
	level: Level
) : ToyEntity(entityType, level) {
}