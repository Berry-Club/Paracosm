package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.paracosm.registry.ModArmorMaterials
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class LightUpShoesItem(properties: Properties) : ArmorItem(ModArmorMaterials.LIGHT_UP_SHOES, Type.BOOTS, properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.isClientSide
			|| entity !is LivingEntity
			|| entity.getItemBySlot(EquipmentSlot.FEET) != stack
		) return

		val pos = entity.blockPosition()
		if (level.isEmptyBlock(pos)) {
			level.setBlock(pos, ModBlocks.LIGHT_UP_SHOES_LIGHT.defaultBlockState(), Block.UPDATE_CLIENTS)
		}
	}

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.FEET

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
	}

}