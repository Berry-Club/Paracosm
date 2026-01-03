package dev.aaronhowser.mods.paracosm.client.render.entity.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.model.DefaultedEntityGeoModel

class PogoStickVehicleModel : DefaultedEntityGeoModel<PogoStickVehicle>(Paracosm.modResource("pogo_stick")) {

	override fun setCustomAnimations(
		animatable: PogoStickVehicle,
		instanceId: Long,
		animationState: AnimationState<PogoStickVehicle>
	) {
		val partialTick = animationState.partialTick

		val verticalRotation = animationProcessor.getBone("vertical_rotation")
		val angle = Mth.lerp(partialTick, animatable.yRotO, animatable.yRot)
		verticalRotation.rotY = angle * Mth.DEG_TO_RAD

		val tiltBackward = Mth.lerp(partialTick, animatable.previousTiltBackward, animatable.tiltBackward)
		val tiltRight = Mth.lerp(partialTick, animatable.previousTiltRight, animatable.tiltRight)

		val tiltPair = OtherUtil.getRotationForCircle(tiltBackward, tiltRight)

		val newBackwardTilt = tiltPair.backwards
		val newRightTilt = tiltPair.right

		val whole = animationProcessor.getBone("whole")
		whole.rotX = newBackwardTilt * PogoStickVehicle.MAX_TILT_RADIAN
		whole.rotZ = newRightTilt * PogoStickVehicle.MAX_TILT_RADIAN

		val jumpPercent = Mth.lerp(partialTick, animatable.previousJumpPercent, animatable.jumpPercent)

		val body = animationProcessor.getBone("body")
		body.posY = -10 * PogoStickVehicle.JUMP_ANIM_DISTANCE.toFloat() * jumpPercent
	}

}