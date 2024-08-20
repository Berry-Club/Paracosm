package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import kotlin.math.abs

class ShrinkRayProjectile(
    entityType: EntityType<out Projectile>,
    level: Level
) : Projectile(entityType, level) {

    constructor(shooter: ServerPlayer, isGrow: Boolean) : this(
        ModEntityTypes.SHRINK_RAY_PROJECTILE.get(),
        shooter.level()
    ) {
        this.isGrow = isGrow

        this.owner = shooter
    }

    var isGrow: Boolean = false

    companion object {
        val IS_GROW: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(
                ShrinkRayProjectile::class.java,
                EntityDataSerializers.BOOLEAN
            )

        const val IS_GROW_NBT = "IsGrow"

        private fun changeEntityScale(
            target: LivingEntity,
            scaleChange: Double,
            changer: Player? = null
        ): Boolean {
            val scaleBefore = target.getAttributeValue(Attributes.SCALE)

            target.shrinkRayEffect += scaleChange

            if (abs(target.shrinkRayEffect) < 0.01) {
                target.shrinkRayEffect = 0.0
            }

            val scaleAfter = target.getAttributeValue(Attributes.SCALE)

            val scaleChanged = scaleBefore != scaleAfter

            if (scaleChanged) {
                target.refreshDimensions()

                val entityName = target.name.string
                val afterString = "%.2f".format(scaleAfter)
                val changeMessage = Component.literal("$entityName scale effect changed to $afterString")

                changer?.displayClientMessage(changeMessage, true)

                if (changer != target && target is Player) {
                    target.displayClientMessage(changeMessage, true)
                }

            }

            return scaleChanged
        }
    }


    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(IS_GROW, isGrow)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean(IS_GROW_NBT, isGrow)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        isGrow = compound.getBoolean(IS_GROW_NBT)
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (!level().isClientSide) {
            remove(RemovalReason.DISCARDED)
        }
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)

        val target = result.entity as? LivingEntity ?: return
        val amount = if (isGrow) 0.1 else -0.1
        val shooter = owner as? Player

        changeEntityScale(target, amount, shooter)
    }

    override fun getDefaultGravity(): Double {
        return 0.0
    }

}