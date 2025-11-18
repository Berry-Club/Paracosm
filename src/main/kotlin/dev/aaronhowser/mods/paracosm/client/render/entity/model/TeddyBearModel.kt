package dev.aaronhowser.mods.paracosm.client.render.entity.model

import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.DefaultedEntityGeoModel

class TeddyBearModel : DefaultedEntityGeoModel<TeddyBearEntity>(OtherUtil.modResource("teddy_bear")) {

	override fun setCustomAnimations(
		animatable: TeddyBearEntity,
		instanceId: Long,
		animationState: AnimationState<TeddyBearEntity>
	) {
		val head: GeoBone = animationProcessor.getBone("head")

		val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA) ?: return

		head.rotX = entityData.headPitch * Mth.DEG_TO_RAD
		head.rotY = entityData.netHeadYaw * Mth.DEG_TO_RAD
	}

}