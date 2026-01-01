package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.AaronExtensions.totalCount
import dev.aaronhowser.mods.aaron.AaronUtil
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

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

		for (i in spawnedEntities.indices) {
			val entity = spawnedEntities[i]
			level.scheduleTaskInTicks(i) {
				entity.playSound(
					SoundEvents.ITEM_PICKUP,
					1f,
					level.random.nextRange(0.2f, 0.5f)
				)
			}
		}

		stack.shrink(1)

		return InteractionResult.sidedSuccess(level.isClientSide)
	}

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (interactionTarget !is ToySoldierEntity) {
			return InteractionResult.PASS
		}

		val soldierStack = interactionTarget.getStack()
		addStackToBucket(stack, soldierStack)

		if (soldierStack.isEmpty) {
			interactionTarget.discard()

			player.playSound(
				SoundEvents.ITEM_PICKUP,
				1f,
				0.33f
			)

			return InteractionResult.sidedSuccess(player.level().isClientSide)
		}

		return InteractionResult.PASS
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

		val added = addStackToBucket(stack, other) > 0
		if (!added) return false

		player.playSound(
			SoundEvents.ITEM_PICKUP,
			1f,
			0.33f
		)

		return true
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
		val storedStacks = contents.nonEmptyItems().toList()

		for (storedStack in storedStacks) {
			val count = storedStack.count
			val itemName = storedStack.hoverName
			tooltipComponents.add(
				Component.empty()
					.append(Component.literal(" - "))
					.append(Component.literal("$count x "))
					.append(itemName)
			)
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		/** @return The amount actually added to the bucket. */
		fun addStackToBucket(
			bucketStack: ItemStack,
			stackToAdd: ItemStack
		): Int {
			if (!bucketStack.isItem(ModItems.TOY_SOLDIER_BUCKET)) return 0

			val currentContents = bucketStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
			val storedStacks = currentContents.nonEmptyItems().toList()

			val countStored = storedStacks.totalCount()
			val maxAmount = ServerConfig.CONFIG.toySoldierBucketMaxStored.get()
			val amountToInsert = stackToAdd.count.coerceAtMost(maxAmount - countStored)

			if (amountToInsert <= 0) return 0

			val stackToInsert = stackToAdd.split(amountToInsert)
			val newContent = AaronUtil.flattenStacks(storedStacks + stackToInsert)

			bucketStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(newContent))

			return amountToInsert
		}

		fun emptyToySoldierBucket(
			stack: ItemStack,
			level: Level,
			position: Vec3,
			placer: Player?
		): List<Entity> {
			val containerContents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
			val storedStacks = containerContents.nonEmptyItemsCopy().toList()

			if (storedStacks.isEmpty()) {
				return emptyList()
			}

			val spawnedEntities = mutableListOf<Entity>()

			var squadLeader: ToySoldierEntity? = null

			for (storedStack in storedStacks) {
				while (storedStack.isNotEmpty()) {
					val dx = level.random.nextRange(-0.01, 0.01)
					val dz = level.random.nextRange(-0.01, 0.01)

					val spawnedEntity = ToySoldierItem.placeToySoldier(
						storedStack,
						level,
						position.add(dx, 0.0, dz),
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

			stack.remove(DataComponents.CONTAINER)

			return spawnedEntities
		}
	}

}