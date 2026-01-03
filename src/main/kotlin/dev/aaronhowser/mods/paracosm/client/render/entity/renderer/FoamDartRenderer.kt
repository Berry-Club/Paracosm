package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.client.render.entity.model.FoamDartModel
import dev.aaronhowser.mods.paracosm.entity.projectile.FoamDartProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

class FoamDartRenderer(
	context: EntityRendererProvider.Context
) : EntityRenderer<FoamDartProjectile>(context) {

	private val model = FoamDartModel(context.bakeLayer(FoamDartModel.LAYER_LOCATION))

	override fun getTextureLocation(entity: FoamDartProjectile): ResourceLocation {
		return TEXTURE
	}

	override fun render(
		entity: FoamDartProjectile,
		entityYaw: Float,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int
	) {
		val lerpedYaw = Mth.lerp(partialTick, entity.yRotO, entity.yRot) - 180
		val lerpedPitch = Mth.lerp(partialTick, entity.xRotO, entity.xRot)

		poseStack.pushPose()
		poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw))
		poseStack.mulPose(Axis.XP.rotationDegrees(lerpedPitch))

		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
		model.renderToBuffer(
			poseStack,
			bufferSource.getBuffer(model.renderType(TEXTURE)),
			packedLight,
			OverlayTexture.NO_OVERLAY
		)

		poseStack.popPose()
	}

	companion object {
		val TEXTURE = Paracosm.modResource("textures/entity/foam_dart.png")
	}

}