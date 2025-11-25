package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.client.render.entity.model.StringWormModel
import dev.aaronhowser.mods.paracosm.entity.StringWormEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class StringWormRenderer(
	renderManager: EntityRendererProvider.Context,
) : GeoEntityRenderer<StringWormEntity>(renderManager, StringWormModel()) {

	init {
		withScale(0.7f)
	}

	override fun getTextureLocation(animatable: StringWormEntity): ResourceLocation = TEXTURE

	companion object {
		val TEXTURE = OtherUtil.modResource("textures/entity/string_worm.png")
	}

}