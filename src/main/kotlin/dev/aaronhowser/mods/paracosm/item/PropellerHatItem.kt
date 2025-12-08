package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import dev.aaronhowser.mods.paracosm.item.base.WearableItem
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class PropellerHatItem(properties: Properties) : WearableItem(properties) {

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (entity !is LivingEntity) return
		val headItem = entity.getItemBySlot(EquipmentSlot.HEAD)
		if (headItem != stack) return

		if (IUpgradeableItem.hasUpgrade(stack, "smooth_flight")) {
			smoothFlightTick(entity)
		} else if (IUpgradeableItem.hasUpgrade(stack, "burst_flight")) {
			burstFlightTick(entity)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)

		//TODO: Whizz sound effect
		// What is it called? the thing that spins, i think?
		// Like this but only the high pitched part: https://www.youtube.com/watch?v=asFIJcLfoos
		private fun burstFlightTick(entity: LivingEntity) {
			if (entity !is Player) return
			if (entity.cooldowns.isOnCooldown(ModItems.PROPELLER_HAT.get())) return
			entity.cooldowns.addCooldown(ModItems.PROPELLER_HAT.get(), 20)

			entity.addDeltaMovement(
				Vec3(
					0.0,
					1.0,
					0.0
				)
			)

			entity.fallDistance = 0f
		}

		//FIXME: Player deltamovement is only on client, so this is probably broken on server
		// The fall distance canceling is DEFINITELY broken on server
		//TODO: Helicopter style sounds
		// Or maybe this: https://www.youtube.com/watch?v=n_xR1M3tGck
		private fun smoothFlightTick(entity: LivingEntity) {
			val movement = entity.deltaMovement

			if (entity.jumping && movement.y < 1) {
				entity.addDeltaMovement(
					Vec3(
						0.0,
						entity.gravity * 1.2,
						0.0
					)
				)
			}

			if (movement.y > -0.05) {
				entity.fallDistance = 0f
			}
		}
	}

}