package dev.aaronhowser.mods.paracosm.item

import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class ToySoldierItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand
		val entityData = stack.get(DataComponents.CUSTOM_DATA) ?: return InteractionResult.PASS

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

		val entityTypeString = entityData.copyTag().getString("id")
		val entityType = level.registryAccess()
			.registryOrThrow(Registries.ENTITY_TYPE)
			.get(ResourceLocation.parse(entityTypeString))

		val entity = entityType?.create(level) ?: return InteractionResult.FAIL

		entityData.loadInto(entity)
		entity.moveTo(posToSpawn.bottomCenter)
		level.addFreshEntity(entity)

		stack.shrink(1)

		return InteractionResult.SUCCESS
	}

}