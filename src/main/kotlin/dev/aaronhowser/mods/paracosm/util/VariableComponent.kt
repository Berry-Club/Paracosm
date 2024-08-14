package dev.aaronhowser.mods.paracosm.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

class VariableComponent(
    val componentOne: Component,
    val componentTwo: Component,
    val condition: () -> Boolean
) : Component {
    override fun getStyle(): Style {
        return if (condition()) componentOne.style else componentTwo.style
    }

    override fun getContents(): ComponentContents {
        return if (condition()) componentOne.contents else componentTwo.contents
    }

    override fun getSiblings(): MutableList<Component> {
        return if (condition()) componentOne.siblings else componentTwo.siblings
    }

    override fun getVisualOrderText(): FormattedCharSequence {
        return if (condition()) componentOne.visualOrderText else componentTwo.visualOrderText
    }
}