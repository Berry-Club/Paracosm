package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item

class HalloweenMaskItem(properties: Properties) : Item(properties), Equipable {

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

}