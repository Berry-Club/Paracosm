package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil.isClientSide
import net.minecraft.client.player.Input
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.VehicleEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
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
        placeOnBlock: BlockPos
    ) : this(ModEntityTypes.POGO_STICK_VEHICLE.get(), level) {
        val blockState = level.getBlockState(placeOnBlock)
        val blockHeight = blockState.getShape(level, placeOnBlock).max(Direction.Axis.Y)

        this.setPos(
            placeOnBlock.x + 0.5,
            placeOnBlock.y + blockHeight,
            placeOnBlock.z + 0.5
        )

    }

    companion object {
        val tiltWest: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
        val tiltNorth: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(tiltWest, 0.0f)
        builder.define(tiltNorth, 0.0f)
    }

    override fun readAdditionalSaveData(p0: CompoundTag) {

    }

    override fun addAdditionalSaveData(p0: CompoundTag) {

    }

    override fun getDropItem(): Item {
        return ModItems.POGO_STICK.get()
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return this.controllingPassenger != entity
    }

    override fun isPushable(): Boolean {
        return true
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

    private var leftImpulse: Float = 0f
    private var forwardImpulse: Float = 0f
    private var up: Boolean = false
    private var down: Boolean = false
    private var left: Boolean = false
    private var right: Boolean = false
    private var jumping: Boolean = false
    private var shiftKeyDown: Boolean = false

    fun setInput(playerInput: Input) {
        this.leftImpulse = playerInput.leftImpulse
        this.forwardImpulse = playerInput.forwardImpulse
        this.up = playerInput.up
        this.down = playerInput.down
        this.left = playerInput.left
        this.right = playerInput.right
        this.jumping = playerInput.jumping
        this.shiftKeyDown = playerInput.shiftKeyDown
    }

    override fun tick() {
        super.tick()
        updateTilt()
    }

    private fun updateTilt() {
        var currentTiltNorth = this.entityData.get(tiltNorth)
        var currentTiltWest = this.entityData.get(tiltWest)

        if (this.forwardImpulse > 0) {
            currentTiltNorth += 0.1f
        } else if (this.forwardImpulse < 0) {
            currentTiltNorth -= 0.1f
        } else {
            currentTiltNorth += if (currentTiltNorth > 0) -0.01f else 0.01f
            if (currentTiltNorth in -0.09..0.09) {
                currentTiltNorth = 0.0f
            }
        }

        if (this.leftImpulse > 0) {
            currentTiltWest += 0.1f
        } else if (this.leftImpulse < 0) {
            currentTiltWest -= 0.1f
        } else {
            currentTiltWest += if (currentTiltWest > 0) -0.05f else 0.05f
            if (currentTiltWest in -0.09..0.09) {
                currentTiltWest = 0.0f
            }
        }

        currentTiltNorth = currentTiltNorth.coerceIn(-1.0f, 1.0f)
        currentTiltWest = currentTiltWest.coerceIn(-1.0f, 1.0f)

        this.entityData.set(tiltNorth, currentTiltNorth)
        this.entityData.set(tiltWest, currentTiltWest)
    }

}