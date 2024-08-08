package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.StringWormEntity
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

    override fun getTextureLocation(animatable: StringWormEntity): ResourceLocation {
        return OtherUtil.modResource("textures/entity/string_worm.png")
    }

}