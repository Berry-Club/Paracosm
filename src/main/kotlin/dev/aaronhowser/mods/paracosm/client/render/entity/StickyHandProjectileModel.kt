package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.StickyHandProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
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

    override fun setCustomAnimations(
        animatable: StickyHandProjectile,
        instanceId: Long,
        animationState: AnimationState<StickyHandProjectile>?
    ) {
        val hand: GeoBone = animationProcessor.getBone("hand")

        hand.rotY = animatable.yRot * Mth.DEG_TO_RAD
        hand.rotX = animatable.xRot * Mth.DEG_TO_RAD
    }
}