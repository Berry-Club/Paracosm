package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

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

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)

        val entity = result.entity
        if (entity is LivingEntity) {
            val vec = entity.position().subtract(this.position())

            entity.knockback(0.5, vec.x, vec.z)

            this.deltaMovement = vec.normalize().scale(-1.0)
        }
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)

        if (result.isInside) {
            this.deltaMovement = this.deltaMovement.scale(-1.0)
            return
        }

        val sideHit = result.direction

        this.addDeltaMovement(
            Vec3(
                sideHit.stepX.toDouble(),
                sideHit.stepY.toDouble(),
                sideHit.stepZ.toDouble()
            )
        )
    }

    override fun tick() {
        super.tick()

        if (this.tickCount > 20 * 10) discard()
    }

}