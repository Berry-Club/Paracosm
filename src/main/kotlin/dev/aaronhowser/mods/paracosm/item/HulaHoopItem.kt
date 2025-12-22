package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

class HulaHoopItem(properties: Properties) : Item(properties), ICurioItem {

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		super.curioTick(slotContext, stack)
	}

}