package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.paracosm.attachment.EntityUpgrades
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack

object Upgradeable {

	// Item

	fun getUpgrades(itemStack: ItemStack): List<String> {
		return itemStack.get(ModDataComponents.ITEM_UPGRADES.get()) ?: emptyList()
	}

	fun setUpgrades(itemStack: ItemStack, upgrades: List<String>) {
		itemStack.set(ModDataComponents.ITEM_UPGRADES.get(), upgrades)
	}

	fun addUpgrade(itemStack: ItemStack, upgrade: String) {
		setUpgrades(
			itemStack,
			getUpgrades(itemStack) + upgrade
		)
	}

	fun removeUpgrade(itemStack: ItemStack, upgrade: String) {
		setUpgrades(
			itemStack,
			getUpgrades(itemStack) - upgrade
		)
	}

	fun hasUpgrade(itemStack: ItemStack, upgrade: String): Boolean {
		return upgrade in getUpgrades(itemStack)
	}

	// Entity

	fun getUpgrades(entity: Entity): Set<String> {
		return entity.getData(ModAttachmentTypes.ENTITY_UPGRADES.get()).upgrades
	}

	fun setUpgrades(entity: Entity, upgrades: Set<String>) {
		entity.setData(ModAttachmentTypes.ENTITY_UPGRADES.get(), EntityUpgrades(upgrades))

		if (entity.isServerSide) {
			val packet = UpdateEntityUpgrades(entity.id, upgrades.toList())
			packet.messageAllPlayersTrackingEntity(entity)
		}
	}

	fun addUpgrade(entity: Entity, upgrade: String) {
		setUpgrades(
			entity,
			getUpgrades(entity) + upgrade
		)
	}

	fun removeUpgrade(entity: Entity, upgrade: String) {
		setUpgrades(
			entity,
			getUpgrades(entity) - upgrade
		)
	}

	fun hasUpgrade(entity: Entity, upgrade: String): Boolean {
		return upgrade in getUpgrades(entity)
	}

}