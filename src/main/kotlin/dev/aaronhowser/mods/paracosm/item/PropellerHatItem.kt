package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.item.base.WearableItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class PropellerHatItem(properties: Properties) : WearableItem(properties) {

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (entity !is LivingEntity) return
		val headItem = entity.getItemBySlot(EquipmentSlot.HEAD)
		if (headItem != stack) return

		val movement = entity.deltaMovement

		if (entity.jumping && movement.y < 1) {
			entity.addDeltaMovement(
				Vec3(
					0.0,
					entity.gravity * 1.2,
					0.0
				)
			)

			// FIXME: Is this only being applied on client, because jumping is only true on client?
			if (movement.y > -2) {
 				entity.fallDistance = 0f
			}
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
	}

}