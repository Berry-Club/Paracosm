package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.client_to_server.UpdatePogoControls
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil.isClientSide
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.entity.vehicle.VehicleEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState

class PogoStickVehicle(
    entityType: EntityType<*>,
    level: Level
) : VehicleEntity(entityType, level), GeoEntity {

    constructor(
        level: Level,
        spawnLocation: Vec3
    ) : this(ModEntityTypes.POGO_STICK_VEHICLE.get(), level) {
        this.setPos(spawnLocation)
    }

    init {
        this.blocksBuilding = true
    }

    companion object {
        val DATA_TILT_RIGHT: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
        val DATA_TILT_BACKWARD: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
        val DATA_JUMP_PERCENT: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)

        const val JUMP_ANIM_DISTANCE = 0.4

        fun checkCancelDamage(event: LivingIncomingDamageEvent) {
            if (event.isCanceled) return

            val source = event.source
            val entity = event.entity
            val level = entity.level()

            if (
                source != level.damageSources().fall()
                && source != level.damageSources().inWall()
            ) return

            if (event.entity.vehicle is PogoStickVehicle) {
                event.isCanceled = true
            }
        }

        private const val UPGRADES = "upgrades"
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_TILT_RIGHT, 0f)
        builder.define(DATA_TILT_BACKWARD, 0f)
        builder.define(DATA_JUMP_PERCENT, 0f)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        val upgradesList = compound.getList(UPGRADES, Tag.TAG_STRING.toInt())
        for (tag in upgradesList) {
            tag as? StringTag ?: continue
            Upgradeable.addUpgrade(this, tag.getAsString())
        }
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        val upgradesList = compound.getList(UPGRADES, Tag.TAG_STRING.toInt())
        for (upgrade in Upgradeable.getUpgrades(this)) {
            upgradesList.add(StringTag.valueOf(upgrade))
        }
    }

    override fun getDropItem(): Item {
        return ModItems.POGO_STICK.get()
    }

    override fun destroy(dropItem: Item) {
        this.kill()
        if (this.level().gameRules.getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            val stack = dropItem.defaultInstance
            stack.set(DataComponents.CUSTOM_NAME, this.customName)

            for (upgrade in Upgradeable.getUpgrades(this)) {
                Upgradeable.addUpgrade(stack, upgrade)
            }

            this.spawnAtLocation(stack)
        }
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return this.hasControllingPassenger()
                && entity.y + (entity.bbHeight * 0.75) < this.y
                && Boat.canVehicleCollide(this, entity)
    }

    override fun isPushable(): Boolean {
        return !this.hasControllingPassenger()
    }

    override fun isPickable(): Boolean {
        return !isRemoved
    }

    // GeckoLib stuff

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, "controller", 0, this::predicate))
    }

    private fun predicate(animationState: AnimationState<PogoStickVehicle>): PlayState {
        return PlayState.STOP
    }

    private val cache = SingletonAnimatableInstanceCache(this)

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return cache
    }

    // Ride stuff

    override fun interact(player: Player, hand: InteractionHand): InteractionResult {
        val defaultResult = super.interact(player, hand)
        if (defaultResult.consumesAction()) {
            return defaultResult
        }

        if (player.isSecondaryUseActive) {
            return InteractionResult.PASS
        }

        if (this.isVehicle) {
            return InteractionResult.PASS
        }

        if (!this.isClientSide) {
            return if (player.startRiding(this)) {
                InteractionResult.CONSUME
            } else {
                InteractionResult.PASS
            }
        }

        return InteractionResult.SUCCESS
    }

    override fun getControllingPassenger(): LivingEntity? {
        return passengers.firstOrNull() as? LivingEntity
    }

    class Controls(
        var leftImpulse: Float = 0f,
        var forwardImpulse: Float = 0f,
        var spaceHeld: Boolean = false
    )

    val controls = Controls()

    fun setInput(leftImpulse: Float, forwardImpulse: Float, jumping: Boolean) {
        this.controls.leftImpulse = leftImpulse
        this.controls.forwardImpulse = forwardImpulse
        this.controls.spaceHeld = jumping

        if (this.isClientSide) {
            ModPacketHandler.messageServer(
                UpdatePogoControls(
                    leftImpulse,
                    forwardImpulse,
                    jumping
                )
            )
        }
    }

    override fun tick() {
        super.tick()

        tryJump()
        doMove()
        updateTilt()
    }

    override fun getDefaultGravity(): Double {
        return 0.04
    }

    private fun doMove() {
        this.applyGravity()
        this.move(MoverType.SELF, this.deltaMovement)

        if (this.onGround()) {
            this.deltaMovement = Vec3.ZERO
        }
    }

    private fun tryResetControls() {
        if (this.hasControllingPassenger()) return

        this.controls.leftImpulse = 0f
        this.controls.forwardImpulse = 0f
        this.controls.spaceHeld = false
    }

    private fun tryJump() {
        if (this.controls.spaceHeld) return

        val currentJumpAmount = this.entityData.get(DATA_JUMP_PERCENT)
        if (currentJumpAmount <= 0.1) return

        if (this.onGround() || Upgradeable.hasUpgrade(this, PogoStickItem.Upgrades.GEPPO)) {
            val currentTiltBack = this.entityData.get(DATA_TILT_BACKWARD)
            val currentTiltRight = this.entityData.get(DATA_TILT_RIGHT)

            val jumpVector = Vec3(0.0, 1.0, 0.0)
                .xRot(currentTiltBack)
                .zRot(currentTiltRight)
                .yRot(this.yRot * Mth.DEG_TO_RAD)
                .scale(currentJumpAmount.toDouble())

            this.deltaMovement = jumpVector
            this.hasImpulse = true
            this.setOnGround(false)
        }

        this.entityData.set(DATA_JUMP_PERCENT, 0.0f)
    }

    private fun updateTilt() {
        val rider = this.controllingPassenger
        if (rider != null) {
            this.setRot(
                -rider.yRot,
                0f
            )
        }

        var currentTiltBackward = this.entityData.get(DATA_TILT_BACKWARD)
        var currentTiltRight = this.entityData.get(DATA_TILT_RIGHT)
        var currentJumpAmount = this.entityData.get(DATA_JUMP_PERCENT)

        if (this.controls.forwardImpulse > 0) {
            currentTiltBackward -= 0.1f
        } else if (this.controls.forwardImpulse < 0) {
            currentTiltBackward += 0.1f
        } else {
            currentTiltBackward += if (currentTiltBackward > 0) -0.01f else 0.01f
            if (currentTiltBackward in -0.09..0.09) {
                currentTiltBackward = 0.0f
            }
        }

        if (this.controls.leftImpulse > 0) {
            currentTiltRight += 0.1f
        } else if (this.controls.leftImpulse < 0) {
            currentTiltRight -= 0.1f
        } else {
            currentTiltRight += if (currentTiltRight > 0) -0.05f else 0.05f
            if (currentTiltRight in -0.09..0.09) {
                currentTiltRight = 0.0f
            }
        }

        if (this.controls.spaceHeld) {
            currentJumpAmount += 0.1f
        }

        currentTiltBackward = currentTiltBackward.coerceIn(-1.0f, 1.0f)
        currentTiltRight = currentTiltRight.coerceIn(-1.0f, 1.0f)
        currentJumpAmount = currentJumpAmount.coerceIn(0.0f, 1.0f)

        this.entityData.set(DATA_TILT_BACKWARD, currentTiltBackward)
        this.entityData.set(DATA_TILT_RIGHT, currentTiltRight)
        this.entityData.set(DATA_JUMP_PERCENT, currentJumpAmount)

        tryResetControls()
    }

    override fun getPassengerAttachmentPoint(entity: Entity, dimensions: EntityDimensions, partialTick: Float): Vec3 {
        val height = 1 - JUMP_ANIM_DISTANCE * this.entityData.get(DATA_JUMP_PERCENT).toDouble()

        return Vec3(0.0, 1.0, 0.0)
            .xRot(this.entityData.get(DATA_TILT_BACKWARD))
            .zRot(this.entityData.get(DATA_TILT_RIGHT))
            .yRot(this.yRot * Mth.DEG_TO_RAD)
            .scale(height)
    }

    override fun getDismountLocationForPassenger(passenger: LivingEntity): Vec3 {
        return this.position()
    }

}