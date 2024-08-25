package dev.aaronhowser.mods.paracosm.entity.custom

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.HangingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class CityRugEntity(
    entityType: EntityType<out HangingEntity>,
    level: Level
) : HangingEntity(entityType, level) {

    companion object {
        const val DEPTH = 0.0625f
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putByte("facing", this.direction.get2DDataValue().toByte())
    }

    override fun dropItem(brokenEntity: Entity?) {
        this.gameEvent(GameEvent.BLOCK_CHANGE, brokenEntity)
    }

    override fun calculateBoundingBox(p0: BlockPos, p1: Direction): AABB {
        val f = 0.46875f
        val vec3 = Vec3.atCenterOf(p0).relative(p1, -0.46875)
        val `direction$axis`: Direction.Axis = p1.axis
        val d0 = if (`direction$axis` === Direction.Axis.X) 0.0625 else 0.75
        val d1 = if (`direction$axis` === Direction.Axis.Y) 0.0625 else 0.75
        val d2 = if (`direction$axis` === Direction.Axis.Z) 0.0625 else 0.75
        return AABB.ofSize(vec3, d0, d1, d2)
    }

    override fun setDirection(facingDirection: Direction) {
        this.direction = facingDirection
        this.xRot = 0.0f
        this.yRot = (direction.get2DDataValue() * 90).toFloat()
        this.xRotO = this.xRot
        this.yRotO = this.yRot
        this.recalculateBoundingBox()
    }

    override fun playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0f, 1.0f)
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        this.direction = Direction.from2DDataValue(tag.getByte("facing").toInt())
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {}
}