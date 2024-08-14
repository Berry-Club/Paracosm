package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import kotlin.math.acos

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

    fun Number.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
        return min2 + (max2 - min2) * ((this.toFloat() - min1) / (max1 - min1))
    }

}