package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.StringWormEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.constant.DataTickets
import software.bernie.geckolib.model.GeoModel

class StringWormModel : GeoModel<StringWormEntity>() {

	override fun getModelResource(animatable: StringWormEntity?): ResourceLocation {
		return OtherUtil.modResource("geo/string_worm.geo.json")
	}

	override fun getTextureResource(animatable: StringWormEntity?): ResourceLocation {
		return OtherUtil.modResource("textures/entity/string_worm.png")
	}

	override fun getAnimationResource(animatable: StringWormEntity?): ResourceLocation {
		return OtherUtil.modResource("animations/string_worm.animation.json")
	}

	override fun setCustomAnimations(
		animatable: StringWormEntity,
		instanceId: Long,
		animationState: AnimationState<StringWormEntity>
	) {
		val head: GeoBone = animationProcessor.getBone("head")

		val entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA) ?: return

		head.rotX = entityData.headPitch * Mth.DEG_TO_RAD
		head.rotY = entityData.netHeadYaw * Mth.DEG_TO_RAD
	}

}