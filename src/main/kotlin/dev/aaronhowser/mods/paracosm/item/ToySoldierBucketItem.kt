package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.AaronExtensions.totalCount
import dev.aaronhowser.mods.aaron.AaronUtil
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

class ToySoldierBucketItem(properties: Properties) : Item(properties) {

	override fun overrideOtherStackedOnMe(
		stack: ItemStack,
		other: ItemStack,
		slot: Slot,
		action: ClickAction,
		player: Player,
		access: SlotAccess
	): Boolean {
		if (action != ClickAction.SECONDARY
			|| !slot.allowModification(player)
			|| !other.isItem(ModItems.TOY_SOLDIER)
		) return false

		val currentContents = stack.get(DataComponents.CONTAINER) ?: ItemContainerContents.fromItems(listOf())
		val storedStacks = currentContents.nonEmptyItems().toList()

		val countStored = storedStacks.totalCount()
		val maxAmount = ServerConfig.CONFIG.toySoldierBucketMaxStored.get()
		val amountToInsert = other.count.coerceAtMost(maxAmount - countStored)
		if (amountToInsert <= 0) return false

		val stackToInsert = other.split(amountToInsert)
		val newContent = AaronUtil.flattenStacks(storedStacks + stackToInsert)

		stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(newContent))

		player.playSound(
			SoundEvents.ITEM_PICKUP,
			1f,
			0.33f
		)

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}