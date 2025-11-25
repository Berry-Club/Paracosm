package dev.aaronhowser.mods.paracosm.client.render.entity.model

import dev.aaronhowser.mods.paracosm.entity.AaronberryEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.OtherUtil.map
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.DefaultedEntityGeoModel

class AaronberryModel : DefaultedEntityGeoModel<AaronberryEntity>(OtherUtil.modResource("aaronberry")) {

	override fun setCustomAnimations(
		animatable: AaronberryEntity,
		instanceId: Long,
		animationState: AnimationState<AaronberryEntity>
	) {
		if (animatable.isHiding) return

		val face: GeoBone = animationProcessor.getBone("face")

		val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA) ?: return

		face.posY = entityData.headPitch.map(-90f, 90f, -3f, 3f)
		face.posX = entityData.netHeadYaw.map(-90f, 90f, -2f, 2f)
	}

}