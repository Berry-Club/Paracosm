package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.StickyHandProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.model.GeoModel

class StickyHandProjectileModel : GeoModel<StickyHandProjectile>() {
    override fun getModelResource(animatable: StickyHandProjectile?): ResourceLocation {
        return OtherUtil.modResource("geo/sticky_hand.geo.json")
    }

    override fun getTextureResource(animatable: StickyHandProjectile?): ResourceLocation {
        return OtherUtil.modResource("textures/entity/sticky_hand.png")
    }

    override fun getAnimationResource(animatable: StickyHandProjectile?): ResourceLocation {
        return OtherUtil.modResource("animations/sticky_hand.animation.json")
    }
}