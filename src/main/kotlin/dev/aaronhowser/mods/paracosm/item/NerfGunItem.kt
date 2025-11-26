package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.item.Item

class NerfGunItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties().stacksTo(1)
	}

}