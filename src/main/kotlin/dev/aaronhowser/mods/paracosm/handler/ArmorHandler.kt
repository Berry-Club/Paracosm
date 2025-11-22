package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Mob
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import kotlin.collections.any

object ArmorHandler {

	fun stopAggro(event: LivingChangeTargetEvent) {
		val attacker = event.entity
		val newTarget = event.originalAboutToBeSetTarget ?: return

		for (armorStack in newTarget.armorSlots) {
			val aggroImmuneFrom = armorStack.get(ModDataComponents.AGGRO_IMMUNE_FROM) ?: continue
			if (aggroImmuneFrom.any { attacker.type.`is`(it) }) {
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
			val nearbyMobs = entity
				.level()
				.getEntitiesOfClass(Mob::class.java, entity.boundingBox.inflate(20.0))

			for (mob in nearbyMobs) {
				if (aggroImmuneFrom.any { mob.type.`is`(it) }) {
					if (mob.target == entity) {
						mob.target = null
					}
				}
			}
		}
	}

}