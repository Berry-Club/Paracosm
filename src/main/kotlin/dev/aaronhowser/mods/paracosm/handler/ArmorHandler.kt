package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Mob
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent

object ArmorHandler {

	fun stopAggro(event: LivingChangeTargetEvent) {
		val attacker = event.entity
		val newTarget = event.originalAboutToBeSetTarget ?: return

		for (armorStack in newTarget.armorSlots) {
			val aggroImmuneFrom = armorStack.get(ModDataComponents.AGGRO_IMMUNE_FROM) ?: continue
			if (attacker.type.`is`(aggroImmuneFrom)) {
				event.isCanceled = true
				return
			}
		}
	}

	fun stopAggro(event: LivingEquipmentChangeEvent) {
		val entity = event.entity

		val newStack = event.to
		val aggroImmuneFrom = newStack.get(ModDataComponents.AGGRO_IMMUNE_FROM)
		if (aggroImmuneFrom != null) {
			entity.level()
				.getEntitiesOfClass(Mob::class.java, entity.boundingBox.inflate(20.0))
				.asSequence()
				.filter { it.type.`is`(aggroImmuneFrom) }
				.filter { it.target == entity }
				.forEach { it.target == null }
		}
	}

}