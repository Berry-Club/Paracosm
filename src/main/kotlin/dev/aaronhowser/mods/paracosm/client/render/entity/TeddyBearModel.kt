package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.GeoModel

class TeddyBearModel : GeoModel<TeddyBearEntity>() {

    override fun getModelResource(animatable: TeddyBearEntity?): ResourceLocation {
        return OtherUtil.modResource("geo/teddy_bear.geo.json")
    }

    override fun getTextureResource(animatable: TeddyBearEntity?): ResourceLocation {
        return OtherUtil.modResource("textures/entity/teddy_bear.png")
    }

    override fun getAnimationResource(animatable: TeddyBearEntity?): ResourceLocation {
        return OtherUtil.modResource("animations/teddy_bear.animation.json")
    }

    override fun setCustomAnimations(
        animatable: TeddyBearEntity,
        instanceId: Long,
        animationState: AnimationState<TeddyBearEntity>
    ) {
        val head: GeoBone = animationProcessor.getBone("head")

        val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA)

        head.rotX = entityData.headPitch * Mth.DEG_TO_RAD
        head.rotY = entityData.netHeadYaw * Mth.DEG_TO_RAD
    }

}