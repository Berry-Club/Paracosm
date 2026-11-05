package dev.aaronhowser.mods.paracosm.util

import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.getWhimsy
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.util.Mth
import net.neoforged.neoforge.client.event.RenderLivingEvent

object ClientUtil {

	fun getLocalWhimsy(): Double = AaronClientUtil.localPlayer?.getWhimsy() ?: 0.0
	fun hasWhimsy(requiredWhimsy: Double): Boolean = getLocalWhimsy() >= requiredWhimsy

	fun renderFlatPre(event: RenderLivingEvent.Pre<*, *>) {
		val entity = event.entity
		if (!entity.getData(ModAttachmentTypes.IS_FLAT)) return

		val stack = event.poseStack
		val torsoRotation = Mth.rotLerp(
			event.partialTick,
			entity.yHeadRotO,
			entity.yHeadRot
		)

		val flatteningRotation = 90f - torsoRotation

		entity.yBodyRot = torsoRotation + 30
		entity.yBodyRotO = torsoRotation + 30

		stack.pushPose()
		stack.mulPose(
			Axis.YP
				.rotationDegrees(flatteningRotation)
		)
		stack.scale(0.01f, 1f, 1f)
		stack.mulPose(
			Axis.YP
				.rotationDegrees(-flatteningRotation)
		)
	}

	fun renderFlatPost(event: RenderLivingEvent.Post<*, *>) {
		val entity = event.entity
		if (!entity.getData(ModAttachmentTypes.IS_FLAT)) return
		event.poseStack.popPose()
	}

}