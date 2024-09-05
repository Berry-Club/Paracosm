package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.GeoModel

class PogoStickVehicleModel : GeoModel<PogoStickVehicle>() {

    override fun getModelResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("geo/pogo_stick.geo.json")
    }

    override fun getTextureResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("textures/entity/pogo_stick.png")
    }

    override fun getAnimationResource(animatable: PogoStickVehicle?): ResourceLocation {
        return OtherUtil.modResource("animations/pogo_stick.animation.json")
    }

    override fun setCustomAnimations(
        animatable: PogoStickVehicle?,
        instanceId: Long,
        animationState: AnimationState<PogoStickVehicle>
    ) {
        val bounceRod = animationProcessor.getBone("bounce_rod")
        val mainBone = animationProcessor.getBone("main")

        if (animatable != null) {
            bounceRod.rotY = animatable.yRot * Mth.DEG_TO_RAD

            bounceRod.rotX = animatable.entityData.get(PogoStickVehicle.DATA_TILT_FORWARD)
            bounceRod.rotZ = animatable.entityData.get(PogoStickVehicle.DATA_TILT_LEFT)

            mainBone.posY =
                -10 * PogoStickVehicle.JUMP_ANIM_DISTANCE.toFloat() * animatable.entityData.get(PogoStickVehicle.DATA_JUMP_PERCENT)
        }
    }

}