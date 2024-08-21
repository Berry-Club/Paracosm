package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.item.StickyHandItem
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.EventHooks
import java.lang.Math.clamp
import kotlin.math.*

class StickyHandProjectile(
    entityType: EntityType<out Projectile>,
    level: Level
) : Projectile(entityType, level) {

    constructor(owner: Player) : this(
        ModEntityTypes.STICKY_HAND_PROJECTILE.get(),
        owner.level()
    ) {
        this.moveTo(owner.x, owner.eyeY, owner.z, owner.yRot, owner.xRot)
        this.owner = owner

        val pitch = owner.xRot
        val yaw = owner.yRot

        var velocity = Vec3(
            -sin(yaw).toDouble(),
            clamp(-sin(pitch) / cos(pitch), -5f, 5f).toDouble(),
            -cos(yaw).toDouble()
        )
        velocity = velocity.scale(0.6 / velocity.length() + this.random.triangle(0.5, 0.0103365))

        this.deltaMovement = velocity
        this.yRot = (atan2(velocity.x, velocity.z) * 180 / PI).toFloat()
        this.xRot = (atan2(velocity.y, velocity.horizontalDistance()) * 180 / PI).toFloat()

        this.yRotO = this.yRot
        this.xRotO = this.xRot
    }

    companion object {
        val HOOKED_ENTITY: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(StickyHandProjectile::class.java, EntityDataSerializers.INT)
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
            val owner = this.owner
            if (owner is Player) {
                StickyHandItem.playerStickyHands.remove(owner)
            }

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

    fun retrieve(): Int {
        if (this.level().isClientSide || this.shouldDiscard()) return 0

        var i = 0
        val grabbedEntity = this.grabbedEntity

        if (grabbedEntity != null) {
            this.pullEntity(grabbedEntity)
            level().broadcastEntityEvent(this, 31.toByte())
            i = if (grabbedEntity is ItemEntity) 3 else 5
        }

        if (this.onGround()) i = 2

        this.discard()

        return i
    }

    override fun handleEntityEvent(id: Byte) {
        val grabbedEntity = this.grabbedEntity
        if (id.toInt() == 31 && level().isClientSide && grabbedEntity is Player && grabbedEntity.isLocalPlayer) {
            this.pullEntity(grabbedEntity)
        }

        super.handleEntityEvent(id)
    }

    private fun pullEntity(grabbedEntity: Entity) {
        val owner = this.owner ?: return

        val vec3 = Vec3(
            owner.x - grabbedEntity.x,
            owner.y - grabbedEntity.y,
            owner.z - grabbedEntity.z
        ).scale(0.1)

        grabbedEntity.deltaMovement = grabbedEntity.deltaMovement.add(vec3)
    }

    private var currentState = StickyHandState.FLYING

    enum class StickyHandState {
        FLYING,
        STUCK_ON_BLOCK,
        STUCK_ON_ENTITY
    }

}