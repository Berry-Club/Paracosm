package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.CityRugEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class CityRugRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<CityRugEntity>(context) {

    companion object {
        val TEXTURE = OtherUtil.modResource("textures/entity/city_rug.png")
    }

    override fun getTextureLocation(p0: CityRugEntity): ResourceLocation {
        return TEXTURE
    }

}