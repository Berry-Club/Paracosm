package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.AaronberryEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.OtherUtil.map
import net.minecraft.resources.ResourceLocation
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
        if (animatable?.isHiding == true) return

        val face: GeoBone? = animationProcessor.getBone("face")

        if (face != null) {
            val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA)

            face.posY = entityData.headPitch.map(-90f, 90f, -3f, 3f)
            face.posX = entityData.netHeadYaw.map(-90f, 90f, -2f, 2f)
        }
    }

}