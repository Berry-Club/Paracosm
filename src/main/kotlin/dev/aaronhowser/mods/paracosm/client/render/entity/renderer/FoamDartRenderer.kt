package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.paracosm.client.render.entity.model.FoamDartModel
import dev.aaronhowser.mods.paracosm.entity.FoamDartProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

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
	}

	companion object {
		val TEXTURE = OtherUtil.modResource("textures/entity/foam_dart.png")
	}

}