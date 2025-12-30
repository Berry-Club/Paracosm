package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isNotTrue
import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.item.component.ToySoldierDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext

class ToySoldierItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand

		val dataComponent = stack.get(ModDataComponents.TOY_SOLDIER) ?: return InteractionResult.PASS

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

		val entity = dataComponent.placeEntity(level, posToSpawn.bottomCenter) ?: return InteractionResult.FAIL

		val player = context.player

		if (entity is ToySoldierEntity) {
			entity.ownerUUID = player?.uuid
		}

		if (player != null) {
			entity.lookAt(EntityAnchorArgument.Anchor.FEET, player.eyePosition)
		}

		if (player?.hasInfiniteMaterials().isNotTrue() || dataComponent.hasCustomData()) {
			stack.shrink(1)
		}

		return InteractionResult.SUCCESS
	}

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (interactionTarget !is ToySoldierEntity) return InteractionResult.PASS

		val level = player.level()

		val dataComponent = stack.get(ModDataComponents.TOY_SOLDIER) ?: return InteractionResult.PASS
		val entity = dataComponent.placeEntity(level, interactionTarget.position()) ?: return InteractionResult.FAIL

		if (entity is ToySoldierEntity) {
			entity.setSquadLeader(interactionTarget)
		}

		entity.lookAt(EntityAnchorArgument.Anchor.FEET, player.eyePosition)

		if (!player.hasInfiniteMaterials() || dataComponent.hasCustomData()) {
			stack.shrink(1)
		}

		return InteractionResult.sidedSuccess(level.isClientSide)
	}

	companion object {
		val DEFAULT_PROPERTIES = {
			Properties()
				.component(ModDataComponents.TOY_SOLDIER.get(), ToySoldierDataComponent(ModEntityTypes.TOY_SOLDIER_GUNNER))
		}
	}

}