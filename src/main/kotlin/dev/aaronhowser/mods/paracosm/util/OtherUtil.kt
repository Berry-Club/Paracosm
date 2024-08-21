package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import kotlin.math.acos
import kotlin.math.pow

object OtherUtil {

    fun modResource(path: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(Paracosm.ID, path)

    fun isLookingAtPos(
        livingEntity: LivingEntity,
        pos: Vec3,
        angleDegrees: Float
    ): Boolean {
        val lookVec = livingEntity.lookAngle

        val entityVec = livingEntity.eyePosition
        val toPosVec = pos.subtract(entityVec).normalize()

        val angleRad = acos(lookVec.dot(toPosVec))
        val angle = Math.toDegrees(angleRad)

        return angle < angleDegrees
    }

    fun LivingEntity.hasLineOfSight(level: Level, vec3: Vec3): Boolean {
        if (this.level() != level) return false

        val entityVec = Vec3(this.x, this.eyeY, this.z)

        return if (vec3.distanceToSqr(entityVec) > ServerConfig.TOY_FLOP_RANGE.get().pow(2)) {
            false
        } else {
            level.clip(
                ClipContext(
                    entityVec,
                    vec3,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    CollisionContext.empty()
                )
            ).type == HitResult.Type.MISS
        }
    }

    fun Number.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this.toFloat() - min1) / (max1 - min1))
    }

}