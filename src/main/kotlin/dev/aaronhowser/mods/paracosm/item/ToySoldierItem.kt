package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class ToySoldierItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand
		val entityData = stack.get(ModDataComponents.TOY_SOLDIER) ?: return InteractionResult.PASS

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

		val entity = entityData.placeEntity(level, posToSpawn.bottomCenter) ?: return InteractionResult.FAIL

		if (entity is ToySoldierEntity) {
			entity.ownerUUID = context.player?.uuid
		}

		return InteractionResult.SUCCESS
	}

}