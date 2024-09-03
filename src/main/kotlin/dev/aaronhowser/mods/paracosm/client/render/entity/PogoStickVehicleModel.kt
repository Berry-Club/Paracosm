package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
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

    override fun applyMolangQueries(animationState: AnimationState<PogoStickVehicle>, animTime: Double) {
        super.applyMolangQueries(animationState, animTime)
        //TODO
    }

    override fun setCustomAnimations(
        animatable: PogoStickVehicle?,
        instanceId: Long,
        animationState: AnimationState<PogoStickVehicle>
    ) {
        val bounceRod: GeoBone? = animationProcessor.getBone("bounce_rod")

        if (bounceRod != null && animatable != null) {
            bounceRod.rotX = animatable.entityData.get(PogoStickVehicle.tiltNorth)
            bounceRod.rotZ = animatable.entityData.get(PogoStickVehicle.tiltEast)
        }

    }

}