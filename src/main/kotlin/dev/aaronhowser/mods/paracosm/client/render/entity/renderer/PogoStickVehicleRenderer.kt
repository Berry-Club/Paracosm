package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.client.render.entity.model.PogoStickVehicleModel
import dev.aaronhowser.mods.paracosm.entity.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class PogoStickVehicleRenderer(
	context: EntityRendererProvider.Context
) : GeoEntityRenderer<PogoStickVehicle>(context, PogoStickVehicleModel()) {

	override fun getTextureLocation(projectile: PogoStickVehicle): ResourceLocation = TEXTURE

	companion object {
		val TEXTURE = Paracosm.modResource("textures/entity/pogo_stick.png")
	}

}