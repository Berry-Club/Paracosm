package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.AaronberryEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.GeoModel

class AaronberryModel : GeoModel<AaronberryEntity>() {

    override fun getModelResource(animatable: AaronberryEntity?): ResourceLocation {
        return OtherUtil.modResource("geo/aaronberry.geo.json")
    }

    override fun getTextureResource(animatable: AaronberryEntity?): ResourceLocation {
        return OtherUtil.modResource("textures/entity/aaronberry.png")
    }

    override fun getAnimationResource(animatable: AaronberryEntity?): ResourceLocation {
        return OtherUtil.modResource("animations/aaronberry.animation.json")
    }

    override fun setCustomAnimations(
        animatable: AaronberryEntity?,
        instanceId: Long,
        animationState: AnimationState<AaronberryEntity>
    ) {
        val head: GeoBone? = animationProcessor.getBone("head")

        if (head != null) {
            val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA)

            head.rotX = entityData.headPitch * Mth.DEG_TO_RAD
            head.rotY = entityData.netHeadYaw * Mth.DEG_TO_RAD
        }
    }

}