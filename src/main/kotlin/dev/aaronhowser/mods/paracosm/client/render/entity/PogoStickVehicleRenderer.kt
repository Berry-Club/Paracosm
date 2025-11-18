package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class PogoStickVehicleRenderer(
	context: EntityRendererProvider.Context
) : GeoEntityRenderer<PogoStickVehicle>(context, PogoStickVehicleModel()) {

	companion object {
		val TEXTURE = OtherUtil.modResource("textures/entity/pogo_stick.png")
	}

	override fun getTextureLocation(projectile: PogoStickVehicle): ResourceLocation {
		return TEXTURE
	}

}