package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class PogoStickModel : GeoModel<PogoStickVehicle>() {

    override fun getModelResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("geo/pogo_stick.geo.json")
    }

    override fun getTextureResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("textures/entity/pogo_stick.png")
    }

    override fun getAnimationResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("animations/pogo_stick.animation.json")
    }
}