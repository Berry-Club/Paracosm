package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.item.base.WearableItem
import net.minecraft.world.entity.EquipmentSlot

class HalloweenMaskItem(properties: Properties) : WearableItem(properties) {

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

}