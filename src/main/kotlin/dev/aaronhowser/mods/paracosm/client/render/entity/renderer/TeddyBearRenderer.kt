package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.client.render.entity.model.TeddyBearModel
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class TeddyBearRenderer(
	renderManager: EntityRendererProvider.Context,
) : GeoEntityRenderer<TeddyBearEntity>(renderManager, TeddyBearModel()) {

	init {
		withScale(0.7f)
	}

	override fun getTextureLocation(animatable: TeddyBearEntity): ResourceLocation {
		return OtherUtil.modResource("textures/entity/teddy_bear.png")
	}

}