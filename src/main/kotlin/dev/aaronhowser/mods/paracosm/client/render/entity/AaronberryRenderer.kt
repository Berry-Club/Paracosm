package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.AaronberryEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.renderer.GeoEntityRenderer

class AaronberryRenderer(
    renderManager: EntityRendererProvider.Context,
) : GeoEntityRenderer<AaronberryEntity>(renderManager, AaronberryModel()) {

    init {
        withScale(0.7f)
    }

    override fun getTextureLocation(animatable: AaronberryEntity): ResourceLocation {
        return OtherUtil.modResource("textures/entity/aaronberry.png")
    }

}