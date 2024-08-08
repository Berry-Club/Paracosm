package dev.aaronhowser.mods.paracosm.client.render.entity

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.paracosm.entity.custom.WhatIsThisAndHowDoIGetRidOfItEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import org.joml.Vector3d
import org.lwjgl.opengl.GL11
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.set
import kotlin.math.cos
import kotlin.math.sin

class WhatIsThisAndHowDoIGetRidOfItEntityRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<WhatIsThisAndHowDoIGetRidOfItEntity>(context) {

    override fun getTextureLocation(p0: WhatIsThisAndHowDoIGetRidOfItEntity): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun getShadowRadius(entity: WhatIsThisAndHowDoIGetRidOfItEntity): Float {
        return 0f
    }

    val gle = CoreGLE()

    @Suppress("KotlinConstantConditions")
    override fun render(
        pEntity: WhatIsThisAndHowDoIGetRidOfItEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {

        GL11.glPushMatrix()

        // rift parts
        val stability = 0f
        val points = pEntity.points
        val pointsSize = points.size

        var amp = 1f
        var stab = Mth.clamp(1f - stability / 50f, 0f, 1.5f)

        GL11.glEnable(3042)

        for (q in 0..3) {

            if (q < 3) {
                GlStateManager._depthMask(false)
                if (q == 0) GL11.glDisable(2929)
            }

            GL11.glBlendFunc(770, if (q < 3) 1 else 771)

            if (pointsSize > 2) {
                GL11.glPushMatrix()

                val pp = Array(pointsSize) { DoubleArray(3) }
                val colors = Array(pointsSize) { FloatArray(4) }
                val radii = DoubleArray(pointsSize)

                for (a in 0 until pointsSize) {

                    var time = pEntity.tickCount + partialTick
                    if (a > pointsSize / 2) time -= a * 10
                    if (a < pointsSize / 2) time += a * 10

                    pp[a][0] = points[a].x + pEntity.x + sin(time / 50f * amp) * 0.10000000149011612 * stab
                    pp[a][1] = points[a].y + pEntity.y + sin(time / 60.0f * amp) * 0.10000000149011612 * stab
                    pp[a][2] = points[a].z + pEntity.z + sin(time / 70.0f * amp) * 0.10000000149011612 * stab

                    colors[a][0] = 1f
                    colors[a][1] = 1f
                    colors[a][2] = 1f
                    colors[a][3] = 1f

                    val w = sin(time / 8f * amp) * 0.10000000149011612 * stab
                    radii[a] = pEntity.pointsWidth[a] * w * (if (q < 3) (1.25f + 0.5f * q) else 1f)
                }

                gle._POLYCYL_TESS = 6
                gle.context.joinStyle = 1026

                gle.glePolyCone(pp.size, pp, colors, radii, 1.0f, 0.0f)

                GL11.glPopMatrix()
            }

        }


    }

    class CoreGLE {
        fun glePolyCone(
            size: Int,
            points: Array<DoubleArray>,
            colors: Array<FloatArray>,
            radii: DoubleArray,
            texSlice: Float,
            start: Float
        ) {

            val xForms = Array(size) { Array(2) { DoubleArray(3) } }

            for (i in 0 until size) {
                xForms[i][0][0] = radii[i]
                xForms[i][0][1] = 0.0
                xForms[i][0][2] = 0.0
                xForms[i][1][0] = points[i][0]
                xForms[i][1][1] = radii[i]
                xForms[i][1][2] = 0.0
            }

            gen_polycone(size, points, colors, 1.0, xForms, texSlice, start)
        }

        var SLICE: Float = 1f
        var SLICE_PROGRESS: Float = 0f
        var _POLYCYL_TESS = 20


        private fun gen_polycone(
            size: Int,
            points: Array<DoubleArray>,
            colors: Array<FloatArray>,
            d: Double,
            xForms: Array<Array<DoubleArray>>?,
            texSlice: Float,
            start: Float
        ) {
            SLICE = texSlice
            SLICE_PROGRESS = start

            var radius = d

            val circle = Array(_POLYCYL_TESS) { Vector2d() }
            val norm = Array(_POLYCYL_TESS) { Vector3d() }
            val v21 = DoubleArray(3)
            var len = 0.0
            val up = DoubleArray(3)

            if (xForms != null) {
                radius = 1.0
            }

            val s = sin(6.283185307179586 / _POLYCYL_TESS)
            val c = cos(6.283185307179586 / _POLYCYL_TESS)

            norm[0].x = 1.0
            norm[0].y = 0.0
            circle[0].x = radius
            circle[0].y = 0.0

            for (i in 1 until _POLYCYL_TESS) {
                norm[i][0] = norm[i - 1][0] * c - norm[i - 1][1] * s
                norm[i][1] = norm[i - 1][0] * s + norm[i - 1][1] * c
                circle[i].x = radius * norm[i][0]
                circle[i].y = radius * norm[i][1]
            }

            var i = 0

            fun findNonDegeneratePoint(
                index: Int,
                npoints: Int,
                len: Double,
                diff: DoubleArray,
                pointArray: Array<DoubleArray>
            ): Int {
                var summa: DoubleArray?
                var i = index
                var tlen = len
                var tdiff: DoubleArray?
                var slen: Double

                do {

                    val a = Vec3(pointArray[i][0], pointArray[i][1], pointArray[i][2])
                    val b = Vec3(pointArray[i + 1][0], pointArray[i + 1][1], pointArray[i + 1][2])
                    val diffAB = a.subtract(b)

                    tdiff = DoubleArray(3).apply {
                        this[0] = diffAB.x
                        this[1] = diffAB.y
                        this[2] = diffAB.z
                    }

                    diff[0] = tdiff[0]
                    diff[1] = tdiff[1]
                    diff[2] = tdiff[2]
                    tlen = matrix.vecLength(diff)
                    summa = matrix.vecSum(pointArray[i + 1], pointArray[i])
                    slen = matrix.vecLength(summa)
                    slen *= 2.0E-6
                    i++
                } while (tlen <= slen && i < npoints - 1)

                return i
            }


        }

        val context = GleContext()

        class GleContext(
            var joinStyle: Int = 0,
            var ncp: Int = 0,
            var contour: Array<DoubleArray>? = null,
            var contourNormal: Array<DoubleArray>? = null,
            var up: DoubleArray? = null,
            var nPoints: Int = 0,
            var pointArray: Array<DoubleArray>? = null,
            var colorArray: Array<FloatArray>? = null,
            var xFormArray: Array<DoubleArray>? = null,
        )


    }

}