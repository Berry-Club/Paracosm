package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Mob
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent

object ArmorHandler {

	//TODO: Make this an upgrade, not just by default
	// Maybe an upgrade to the PLAYER that makes it so that any worn armor has this effect,
	// rather than having it be per armor stack?

	fun stopAggro(event: LivingChangeTargetEvent) {
		val attacker = event.entity
		val newTarget = event.originalAboutToBeSetTarget ?: return

		for (armorStack in newTarget.armorSlots) {
			val aggroImmuneFrom = armorStack.get(ModDataComponents.AGGRO_IMMUNE_FROM) ?: continue
			if (attacker.type.`is`(aggroImmuneFrom)) {
				event.isCanceled = true
				break
			}
		}
	}

	fun stopAggro(event: LivingEquipmentChangeEvent) {
		if (!event.slot.isArmor) return

		val aggroImmuneFrom = event
			.to
			.get(ModDataComponents.AGGRO_IMMUNE_FROM)
			?: return

		val entity = event.entity
		val nearbyMobs = entity.level()
			.getEntitiesOfClass(Mob::class.java, entity.boundingBox.inflate(20.0))

		for (mob in nearbyMobs) {
			if (mob.type.`is`(aggroImmuneFrom) && mob.target == entity) {
				mob.target = null
			}
		}
	}

}