package dev.aaronhowser.mods.paracosm.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

class VariableComponent(
	val componentTrue: Component,
	val componentFalse: Component,
	val condition: () -> Boolean
) : Component {
	override fun getStyle(): Style {
		return if (condition()) componentTrue.style else componentFalse.style
	}

	override fun getContents(): ComponentContents {
		return if (condition()) componentTrue.contents else componentFalse.contents
	}

	override fun getSiblings(): MutableList<Component> {
		return if (condition()) componentTrue.siblings else componentFalse.siblings
	}

	override fun getVisualOrderText(): FormattedCharSequence {
		return if (condition()) componentTrue.visualOrderText else componentFalse.visualOrderText
	}
}