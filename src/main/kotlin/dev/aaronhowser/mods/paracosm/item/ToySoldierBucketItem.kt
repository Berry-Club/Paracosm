package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.AaronExtensions.totalCount
import dev.aaronhowser.mods.aaron.AaronUtil
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.core.Position
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class ToySoldierBucketItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand

		val level = context.level

		val clickedPos = context.clickedPos
		val clickedFace = context.clickedFace
		val clickedState = level.getBlockState(clickedPos)

		val posToSpawn = if (!clickedState.isSuffocating(level, clickedPos)) {
			clickedPos
		} else {
			val relative = clickedPos.relative(clickedFace)
			if (level.getBlockState(relative).isSuffocating(level, relative)) {
				return InteractionResult.FAIL
			}

			relative
		}

		val spawnedEntities = emptyToySoldierBucket(
			stack,
			level,
			posToSpawn.bottomCenter,
			context.player
		)

		if (spawnedEntities.isEmpty()) {
			return InteractionResult.PASS
		}

		stack.shrink(1)

		return InteractionResult.sidedSuccess(level.isClientSide)
	}

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
			|| !other.has(ModDataComponents.TOY_SOLDIER)
		) return false

		val currentContents = stack.get(DataComponents.CONTAINER)
		val storedStacks = currentContents?.nonEmptyItems()?.toList() ?: emptyList()

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

		fun emptyToySoldierBucket(
			stack: ItemStack,
			level: Level,
			position: Position,
			placer: Player?
		): List<Entity> {
			val containerContents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
			val storedStacks = containerContents.nonEmptyItems().toList()

			if (storedStacks.isEmpty()) {
				return emptyList()
			}

			val spawnedEntities = mutableListOf<Entity>()

			var squadLeader: ToySoldierEntity? = null

			for (storedStack in storedStacks) {
				while (storedStack.isNotEmpty()) {
					val spawnedEntity = ToySoldierItem.placeToySoldier(
						storedStack,
						level,
						position,
						squadLeader,
						placer
					)

					if (spawnedEntity != null) {
						spawnedEntities.add(spawnedEntity)
					}

					if (squadLeader == null && spawnedEntity is ToySoldierEntity) {
						squadLeader = spawnedEntity
					}

					storedStack.shrink(1)
				}
			}

			return spawnedEntities
		}
	}

}