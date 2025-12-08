package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.handler.KeyHandler
import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import dev.aaronhowser.mods.paracosm.item.base.WearableItem
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class PropellerHatItem(properties: Properties) : WearableItem(properties), IUpgradeableItem {

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

	override val possibleUpgrades: List<String> = listOf(
		SMOOTH_FLIGHT_UPGRADE,
		BURST_FLIGHT_UPGRADE
	)

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (entity !is Player) return
		val headItem = entity.getItemBySlot(EquipmentSlot.HEAD)
		if (headItem != stack) return

		if (IUpgradeableItem.hasUpgrade(stack, SMOOTH_FLIGHT_UPGRADE)) {
			smoothFlightTick(entity)
		} else if (IUpgradeableItem.hasUpgrade(stack, BURST_FLIGHT_UPGRADE)) {
			burstFlightTick(entity)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)

		const val SMOOTH_FLIGHT_UPGRADE = "smooth_flight"
		const val BURST_FLIGHT_UPGRADE = "burst_flight"

		//TODO: Whizz sound effect
		// What is it called? the thing that spins, i think?
		// Like this but only the high pitched part: https://www.youtube.com/watch?v=asFIJcLfoos
		private fun burstFlightTick(player: Player) {
			if (!KeyHandler.isHoldingSpace(player)
				|| player.cooldowns.isOnCooldown(ModItems.PROPELLER_HAT.get())
			) return

			player.cooldowns.addCooldown(ModItems.PROPELLER_HAT.get(), 20)

			player.addDeltaMovement(
				Vec3(
					0.0,
					1.0,
					0.0
				)
			)

			player.fallDistance = 0f
		}

		//TODO: Helicopter style sounds
		// Or maybe this: https://www.youtube.com/watch?v=n_xR1M3tGck
		private fun smoothFlightTick(player: Player) {
			val movement = player.deltaMovement

			if (KeyHandler.isHoldingSpace(player) && movement.y < 1) {
				player.addDeltaMovement(
					Vec3(
						0.0,
						player.gravity * 1.2,
						0.0
					)
				)
			}

			player.fallDistance = 0f
		}
	}

}