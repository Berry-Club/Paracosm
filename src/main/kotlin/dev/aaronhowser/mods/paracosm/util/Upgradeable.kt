package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.item.component.StringListComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.item.ItemStack

object Upgradeable {

    fun getUpgrades(itemStack: ItemStack): Set<String> {
        return itemStack.get(ModDataComponents.UPGRADES.get())?.value ?: emptySet()
    }

    fun addUpgrade(itemStack: ItemStack, upgrade: String) {
        val upgrades = getUpgrades(itemStack).toMutableSet()
        upgrades.add(upgrade)
        itemStack.set(ModDataComponents.UPGRADES.get(), StringListComponent(upgrades))
    }

    fun removeUpgrade(itemStack: ItemStack, upgrade: String) {
        val upgrades = getUpgrades(itemStack).toMutableSet()
        upgrades.remove(upgrade)
        itemStack.set(ModDataComponents.UPGRADES.get(), StringListComponent(upgrades))
    }

    fun hasUpgrade(itemStack: ItemStack, upgrade: String): Boolean {
        return getUpgrades(itemStack).contains(upgrade)
    }

}