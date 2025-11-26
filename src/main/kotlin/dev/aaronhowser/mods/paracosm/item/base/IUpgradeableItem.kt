package dev.aaronhowser.mods.paracosm.item.base

import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.item.ItemStack

interface IUpgradeableItem {

	val possibleUpgrades: List<String>

	companion object {
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
	}

}