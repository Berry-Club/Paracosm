package dev.aaronhowser.mods.paracosm.entity.custom

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import java.util.*
import kotlin.jvm.optionals.getOrNull

class StickyHandProjectile(
    entityType: EntityType<out Projectile>,
    level: Level
) : Projectile(entityType, level) {

    companion object {
        val OWNER_UUID: EntityDataAccessor<Optional<UUID>> =
            SynchedEntityData.defineId(
                ShrinkRayProjectile::class.java,
                EntityDataSerializers.OPTIONAL_UUID
            )

        const val OWNER_UUID_NBT = "OwnerUUID"
    }

    var owner: Player?
        get() {
            val uuid = this.entityData.get(OWNER_UUID).getOrNull() ?: return null

            return this.server?.playerList?.getPlayer(uuid)
        }
        set(value) {
            val optional = if (value == null) {
                Optional.empty()
            } else {
                Optional.of(value.uuid)
            }

            this.entityData.set(OWNER_UUID, optional)
        }

    fun setOwner(ownerUuid: UUID) {
        this.owner = this.server?.playerList?.getPlayer(ownerUuid)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(OWNER_UUID, Optional.empty())
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        val owner = this.owner
        if (owner != null) {
            compound.putUUID(OWNER_UUID_NBT, owner.uuid)
        }
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        if (compound.contains(OWNER_UUID_NBT)) {
            this.setOwner(compound.getUUID(OWNER_UUID_NBT))
        }
    }

}