package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import net.minecraft.world.entity.LivingEntity

object AttributeHandler {

	fun LivingEntity.getWhimsy(): Double = this.getAttributeValue(ModAttributes.WHIMSY)
	fun LivingEntity.getDelusion(): Double = this.getAttributeValue(ModAttributes.DELUSION)

}