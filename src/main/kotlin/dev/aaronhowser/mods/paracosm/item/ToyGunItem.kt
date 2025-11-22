package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.paracosm.datagen.language.ModItemLang
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import dev.aaronhowser.mods.paracosm.util.TrueFalseComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ToyGunItem(properties: Properties) : RequiresWhimsy, Item(properties) {

	override val requiredWhimsy: Double = 5.0

	private val nameComponent = TrueFalseComponent(
		componentTrue = ModItemLang.COOL_GUN.toComponent(),
		componentFalse = ModItemLang.TOY_GUN.toComponent()
	) { ClientUtil.hasWhimsy(requiredWhimsy) }

	override fun getName(stack: ItemStack): Component {
		return nameComponent
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}