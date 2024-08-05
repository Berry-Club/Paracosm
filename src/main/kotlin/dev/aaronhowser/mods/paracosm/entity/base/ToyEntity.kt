package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity

abstract class ToyEntity(
    entityType: EntityType<out TamableAnimal>,
    level: Level
) : TamableAnimal(entityType, level), RequiresWhimsy, GeoEntity