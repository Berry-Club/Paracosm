package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.paracosm.datagen.language.ModItemLang
import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.item.component.ToySoldierDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.Position
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

//FIXME: stack shrinking isn't working when in creative mode
class ToySoldierItem(properties: Properties) : Item(properties) {

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

		val success = placeToySoldier(stack, level, posToSpawn.bottomCenter, null, context.player) != null

		if (!success) return InteractionResult.PASS

		stack.shrink(1)

		return InteractionResult.sidedSuccess(level.isClientSide)
	}

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (interactionTarget !is ToySoldierEntity) return InteractionResult.PASS

		val level = player.level()

		val success = placeToySoldier(stack, level, interactionTarget.position(), interactionTarget, player) != null

		if (!success) return InteractionResult.PASS

		stack.shrink(1)

		return InteractionResult.sidedSuccess(level.isClientSide)
	}

	override fun getName(stack: ItemStack): Component {
		val soldierComponent = stack.get(ModDataComponents.TOY_SOLDIER) ?: return super.getName(stack)

		val entityType = soldierComponent.type.value()

		val soldierTypeName = when (entityType) {
			ModEntityTypes.TOY_SOLDIER_GUNNER.get() -> ModItemLang.TOY_SOLDIER_GUNNER.toComponent()
			else -> ModItemLang.TOY_SOLDER_UNKNOWN.toComponent()
		}

		return ModItemLang.TOY_SOLDIER.toComponent(soldierTypeName)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.component(
					ModDataComponents.TOY_SOLDIER.get(),
					ToySoldierDataComponent(ModEntityTypes.TOY_SOLDIER_GUNNER.get())
				)
		}

		fun placeToySoldier(
			stack: ItemStack,
			level: Level,
			pos: Position,
			clickedSoldier: ToySoldierEntity?,
			placer: Player?
		): Entity? {
			val soldierComponent = stack.get(ModDataComponents.TOY_SOLDIER) ?: return null
			val placedEntity = soldierComponent.placeEntity(level, pos) ?: return null

			val name = stack.get(DataComponents.CUSTOM_NAME)
			if (name != null) {
				placedEntity.customName = name
			}

			if (placedEntity is ToySoldierEntity) {
				placedEntity.ownerUUID = placer?.uuid

				if (clickedSoldier != null) {
					placedEntity.setSquadLeader(clickedSoldier)
				}

				if (placer != null) {
					placedEntity.lookAt(EntityAnchorArgument.Anchor.FEET, placer.eyePosition)
				}
			}

			return placedEntity
		}
	}

}