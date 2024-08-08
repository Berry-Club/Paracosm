package dev.aaronhowser.mods.paracosm.entity.custom

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class WhatIsThisAndHowDoIGetRidOfItEntity(
    entityType: EntityType<*>,
    level: Level
) : Entity(entityType, level) {

    val points: ArrayList<Vec3> = ArrayList()
    val pointsWidth: ArrayList<Float> = ArrayList()

    override fun defineSynchedData(p0: SynchedEntityData.Builder) {
        TODO("Not yet implemented")
    }

    override fun readAdditionalSaveData(p0: CompoundTag) {
        TODO("Not yet implemented")
    }

    override fun addAdditionalSaveData(p0: CompoundTag) {
        TODO("Not yet implemented")
    }
}