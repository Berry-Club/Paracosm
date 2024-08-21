package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class StickyHandProjectile(
    entityType: EntityType<out Projectile>,
    level: Level
) : Projectile(entityType, level) {

    constructor(owner: Player) : this(
        ModEntityTypes.STICKY_HAND_PROJECTILE.get(),
        owner.level()
    ) {
        this.moveTo(owner.x, owner.eyeY, owner.z)
        this.owner = owner
    }

    override fun defineSynchedData(p0: SynchedEntityData.Builder) {}


}