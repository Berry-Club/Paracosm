package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.attachment.EntityUpgrades
import dev.aaronhowser.mods.paracosm.item.component.StringListComponent
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack

object Upgradeable {

    // Item

    fun getUpgrades(itemStack: ItemStack): Set<String> {
        return itemStack.get(ModDataComponents.ITEM_UPGRADES.get())?.value ?: emptySet()
    }

    fun addUpgrade(itemStack: ItemStack, upgrade: String) {
        val upgrades = getUpgrades(itemStack).toMutableSet()
        upgrades.add(upgrade)
        itemStack.set(ModDataComponents.ITEM_UPGRADES.get(), StringListComponent(upgrades))
    }

    fun removeUpgrade(itemStack: ItemStack, upgrade: String) {
        val upgrades = getUpgrades(itemStack).toMutableSet()
        upgrades.remove(upgrade)
        itemStack.set(ModDataComponents.ITEM_UPGRADES.get(), StringListComponent(upgrades))
    }

    fun hasUpgrade(itemStack: ItemStack, upgrade: String): Boolean {
        return getUpgrades(itemStack).contains(upgrade)
    }

    // Entity

    fun getUpgrades(entity: Entity): Set<String> {
        return entity.getData(ModAttachmentTypes.ENTITY_UPGRADES.get()).upgrades
    }

    fun addUpgrade(entity: Entity, upgrade: String) {
        val upgrades = getUpgrades(entity).toMutableSet()
        upgrades.add(upgrade)
        entity.setData(ModAttachmentTypes.ENTITY_UPGRADES.get(), EntityUpgrades(upgrades))
    }

    fun removeUpgrade(entity: Entity, upgrade: String) {
        val upgrades = getUpgrades(entity).toMutableSet()
        upgrades.remove(upgrade)
        entity.setData(ModAttachmentTypes.ENTITY_UPGRADES.get(), EntityUpgrades(upgrades))
    }

    fun hasUpgrade(entity: Entity, upgrade: String): Boolean {
        return getUpgrades(entity).contains(upgrade)
    }

}