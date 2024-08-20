package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import java.util.*

class ShrinkRayProjectile(
    entityType: EntityType<out Projectile>,
    level: Level
) : Projectile(entityType, level) {

    constructor(shooter: ServerPlayer, isGrow: Boolean) : this(
        ModEntityTypes.SHRINK_RAY_PROJECTILE.get(),
        shooter.level()
    ) {
        this.isGrow = isGrow
        this.shooterUuid = Optional.of(shooter.uuid)
    }

    var isGrow: Boolean = false
    var shooterUuid: Optional<UUID> = Optional.empty()

    companion object {
        val IS_GROW: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(
                ShrinkRayProjectile::class.java,
                EntityDataSerializers.BOOLEAN
            )

        val SHOOTER: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(
                ShrinkRayProjectile::class.java,
                EntityDataSerializers.OPTIONAL_UUID
            )

        const val IS_GROW_NBT = "IsGrow"
        const val SHOOTER_UUID_NBT = "ShooterUUID"
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(IS_GROW, isGrow)
        builder.define(SHOOTER, shooterUuid)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putBoolean(IS_GROW_NBT, isGrow)
        shooterUuid.ifPresent { compound.putUUID(SHOOTER_UUID_NBT, it) }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        isGrow = compound.getBoolean(IS_GROW_NBT)
        shooterUuid = if (compound.contains(SHOOTER_UUID_NBT)) {
            Optional.of(compound.getUUID(SHOOTER_UUID_NBT))
        } else {
            Optional.empty()
        }
    }

}