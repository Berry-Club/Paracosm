package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
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

    companion object {
        val tiltX: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
        val tiltZ: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(PogoStickVehicle::class.java, EntityDataSerializers.FLOAT)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(tiltX, 0.0f)
        builder.define(tiltZ, 0.0f)
    }

    override fun readAdditionalSaveData(p0: CompoundTag) {

    }

    override fun addAdditionalSaveData(p0: CompoundTag) {

    }

    override fun getDropItem(): Item {
        return ModItems.POGO_STICK.get()
    }

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
}