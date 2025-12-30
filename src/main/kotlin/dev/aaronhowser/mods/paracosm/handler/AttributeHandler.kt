package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import net.minecraft.world.entity.LivingEntity

object AttributeHandler {

	fun LivingEntity.getWhimsy(): Double = getAttributeValue(ModAttributes.WHIMSY)
	fun LivingEntity.getDelusion(): Double = getAttributeValue(ModAttributes.DELUSION)

	var LivingEntity.baseWhimsy: Double
		get() = getAttribute(ModAttributes.WHIMSY)?.baseValue ?: 0.0
		set(value) {
			getAttribute(ModAttributes.WHIMSY)?.baseValue = value
		}

	var LivingEntity.baseDelusion: Double
		get() = getAttribute(ModAttributes.DELUSION)?.baseValue ?: 0.0
		set(value) {
			getAttribute(ModAttributes.DELUSION)?.baseValue = value
		}

}