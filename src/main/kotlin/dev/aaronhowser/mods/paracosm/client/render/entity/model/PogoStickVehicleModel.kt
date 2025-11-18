package dev.aaronhowser.mods.paracosm.client.render.entity.model

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.DefaultedEntityGeoModel

class PogoStickVehicleModel : DefaultedEntityGeoModel<PogoStickVehicle>(OtherUtil.modResource("pogo_stick")) {

	override fun setCustomAnimations(
		animatable: PogoStickVehicle,
		instanceId: Long,
		animationState: AnimationState<PogoStickVehicle>
	) {
		val verticalRotation = animationProcessor.getBone("vertical_rotation")
		verticalRotation.rotY = animatable.yRot * Mth.DEG_TO_RAD

		val tiltBackward = animatable.entityData.get(PogoStickVehicle.DATA_TILT_BACKWARD)
		val tiltRight = animatable.entityData.get(PogoStickVehicle.DATA_TILT_RIGHT)

		val tiltPair = OtherUtil.getRotationForCircle(tiltBackward, tiltRight)

		val newBackwardTilt = tiltPair.backwards
		val newRightTilt = tiltPair.right

		val whole = animationProcessor.getBone("whole")
		whole.rotX = newBackwardTilt * PogoStickVehicle.MAX_TILT_RADIAN
		whole.rotZ = newRightTilt * PogoStickVehicle.MAX_TILT_RADIAN

		val body = animationProcessor.getBone("body")
		body.posY = -10 *
				PogoStickVehicle.JUMP_ANIM_DISTANCE.toFloat() *
				animatable.entityData.get(PogoStickVehicle.DATA_JUMP_PERCENT)
	}

}