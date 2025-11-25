package dev.aaronhowser.mods.paracosm.client.render.entity.model

import dev.aaronhowser.mods.paracosm.entity.StickyHandProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedEntityGeoModel

class StickyHandProjectileModel : DefaultedEntityGeoModel<StickyHandProjectile>(OtherUtil.modResource("sticky_hand")) {

	override fun setCustomAnimations(
		animatable: StickyHandProjectile,
		instanceId: Long,
		animationState: AnimationState<StickyHandProjectile>?
	) {
		val hand: GeoBone = animationProcessor.getBone("hand")

		hand.rotY = animatable.yRot * Mth.DEG_TO_RAD
		hand.rotX = animatable.xRot * Mth.DEG_TO_RAD
	}
}