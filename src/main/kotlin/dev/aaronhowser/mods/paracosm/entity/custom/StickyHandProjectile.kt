package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.FishingHook
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.EventHooks
import kotlin.math.pow

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

    companion object {
        val HOOKED_ENTITY: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(FishingHook::class.java, EntityDataSerializers.INT)
    }

    var grabbedEntity: Entity? = null

    override fun defineSynchedData(p0: SynchedEntityData.Builder) {
        p0.define(HOOKED_ENTITY, 0)
    }

    override fun onSyncedDataUpdated(key: EntityDataAccessor<*>) {
        if (HOOKED_ENTITY == key) {
            val entityId = this.entityData.get(HOOKED_ENTITY)
            this.grabbedEntity = if (entityId > 0) level().getEntity(entityId - 1) else null
        }
    }

    var life = 0

    override fun tick() {
        super.tick()

        if (level().isClientSide) return

        if (shouldDiscard()) {
            this.discard()
            return
        }

        if (this.onGround()) {
            this.life++
        } else {
            this.life = 0
        }

        if (this.currentState == StickyHandState.FLYING) {
            if (this.grabbedEntity != null) {
                this.currentState = StickyHandState.STUCK_ON_ENTITY
                this.deltaMovement = Vec3.ZERO
                return
            }

            this.checkCollision()
        } else {
            if (this.currentState == StickyHandState.STUCK_ON_ENTITY) {
                val grabbedEntity = this.grabbedEntity
                if (grabbedEntity != null) {
                    if (grabbedEntity.isRemoved || grabbedEntity.level() != this.level()) {
                        this.grabbedEntity = null
                        this.currentState = StickyHandState.FLYING
                    } else {
                        this.setPos(grabbedEntity.x, grabbedEntity.getY(0.8), grabbedEntity.z)
                    }
                }

                return
            }
        }

        this.move(MoverType.SELF, this.deltaMovement)
        this.updateRotation()

        if (this.currentState == StickyHandState.FLYING && (this.onGround() || this.horizontalCollision)) {
            this.deltaMovement = Vec3.ZERO
        }

        this.deltaMovement = this.deltaMovement.scale(0.92) //What?
        this.reapplyPosition()
    }

    private fun checkCollision() {
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)

        if (hitResult.type == HitResult.Type.MISS || !EventHooks.onProjectileImpact(this, hitResult)) {
            this.onHit(hitResult)
        }
    }

    private fun shouldDiscard(): Boolean {
        val player = owner as? Player ?: return true

        if (this.life > 20 * 10) {
            return true
        }

        val mainHandItem = player.mainHandItem
        val offHandItem = player.offhandItem

        if (mainHandItem.item != ModItems.STICKY_HAND.get() && offHandItem.item != ModItems.STICKY_HAND.get()) {
            return true
        }

        if (player.isRemoved || !player.isAlive || player.distanceToSqr(this) > 64.0.pow(2)) {
            return true
        }

        return false
    }

    private var currentState = StickyHandState.FLYING

    enum class StickyHandState {
        FLYING,
        STUCK_ON_BLOCK,
        STUCK_ON_ENTITY
    }

}