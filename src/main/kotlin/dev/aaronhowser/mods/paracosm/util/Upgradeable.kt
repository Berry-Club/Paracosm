package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.attachment.EntityUpgrades
import dev.aaronhowser.mods.paracosm.item.component.StringListComponent
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.util.OtherUtil.isClientSide
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

object Upgradeable {

    // Item

    fun getUpgrades(itemStack: ItemStack): Set<String> {
        return itemStack.get(ModDataComponents.ITEM_UPGRADES.get())?.value ?: emptySet()
    }

    fun addUpgrade(itemStack: ItemStack, upgrade: String) {
        itemStack.set(
            ModDataComponents.ITEM_UPGRADES.get(),
            StringListComponent(getUpgrades(itemStack) + upgrade)
        )
    }

    fun removeUpgrade(itemStack: ItemStack, upgrade: String) {
        itemStack.set(
            ModDataComponents.ITEM_UPGRADES.get(),
            StringListComponent(getUpgrades(itemStack) - upgrade)
        )
    }

    fun setUpgrades(itemStack: ItemStack, upgrades: Set<String>) {
        itemStack.set(ModDataComponents.ITEM_UPGRADES.get(), StringListComponent(upgrades))
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

        if (!entity.isClientSide) {
            ModPacketHandler.messageNearbyPlayers(
                UpdateEntityUpgrades(entity.id, upgrades.toList()),
                entity.level() as ServerLevel,
                entity.blockPosition().toVec3(),
                64.0
            )
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