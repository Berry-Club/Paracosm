package dev.aaronhowser.mods.paracosm.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemContainerContents

class ToySoldierBucketItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES = {
			Properties().stacksTo(1)
				.component(
					DataComponents.CONTAINER,
					ItemContainerContents.fromItems(emptyList())
				)
		}
	}

}