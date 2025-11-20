package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import net.minecraft.world.entity.LivingEntity

object AttributeHandler {

	fun LivingEntity.getWhimsy(): Double = this.getAttributeValue(ModAttributes.WHIMSY)
	fun LivingEntity.getDelusion(): Double = this.getAttributeValue(ModAttributes.DELUSION)

	var LivingEntity.baseWhimsy: Double
		get() = this.getAttribute(ModAttributes.WHIMSY)?.baseValue ?: 0.0
		set(value) {
			this.getAttribute(ModAttributes.WHIMSY)?.baseValue = value
		}

	var LivingEntity.baseDelusion: Double
		get() = this.getAttribute(ModAttributes.DELUSION)?.baseValue ?: 0.0
		set(value) {
			this.getAttribute(ModAttributes.DELUSION)?.baseValue = value
		}

}