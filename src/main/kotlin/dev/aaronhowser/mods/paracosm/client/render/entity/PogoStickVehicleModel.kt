package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.compatibility.geckolib.ModMolangQueries
import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.loading.math.MathParser
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
        animatable: PogoStickVehicle,
        instanceId: Long,
        animationState: AnimationState<PogoStickVehicle>
    ) {
        val whole = animationProcessor.getBone("whole")
        val body = animationProcessor.getBone("body")

        whole.rotY = animatable.yRot * Mth.DEG_TO_RAD
        body.posY =
            -10 * PogoStickVehicle.JUMP_ANIM_DISTANCE.toFloat() * animatable.entityData.get(PogoStickVehicle.DATA_JUMP_PERCENT)
    }

    override fun applyMolangQueries(animationState: AnimationState<PogoStickVehicle>, animTime: Double) {
        val pogo = animationState.animatable

        val forward = pogo.entityData.get(PogoStickVehicle.DATA_TILT_FORWARD).toDouble()
        val left = pogo.entityData.get(PogoStickVehicle.DATA_TILT_LEFT).toDouble()

        MathParser.setVariable(ModMolangQueries.TILT_FORWARD) { forward }
        MathParser.setVariable(ModMolangQueries.TILT_LEFT) { left }

        if (forward != 0.0) {
            println("forward $forward")
            println(MathParser.getVariableFor(ModMolangQueries.TILT_FORWARD))
        }
        if (left != 0.0) {
            println("left $left")
            println(MathParser.getVariableFor(ModMolangQueries.TILT_LEFT))
        }
    }

}