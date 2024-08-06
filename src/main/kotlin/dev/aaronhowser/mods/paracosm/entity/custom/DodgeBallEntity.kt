package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class DodgeBallEntity : ThrowableItemProjectile {

    constructor(
        entityType: EntityType<out ThrowableItemProjectile>,
        level: Level
    ) : super(entityType, level)

    constructor(
        level: Level,
        x: Double,
        y: Double,
        z: Double
    ) : super(ModEntityTypes.DODGE_BALL.get(), x, y, z, level)

    constructor(
        level: Level,
        shooter: LivingEntity
    ) : super(ModEntityTypes.DODGE_BALL.get(), shooter, level)

    override fun getDefaultItem(): Item {
        return ModItems.DODGE_BALL.get()
    }
}