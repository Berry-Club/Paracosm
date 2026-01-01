package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.language.ModBlockLang
import dev.aaronhowser.mods.paracosm.datagen.language.ModEntityTypeLang
import dev.aaronhowser.mods.paracosm.datagen.language.ModItemLang
import dev.aaronhowser.mods.paracosm.datagen.language.ModSubtitleLang
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(output: PackOutput) : LanguageProvider(output, Paracosm.MOD_ID, "en_us") {

	object Curios {
		const val SEEING_STONE = "curios.identifier.seeing_stone"
	}

	object Emi {
		const val SEEING_STONE_TAG = "tag.item.curios.seeing_stone"
		const val SWEETS_TAG = "tag.item.paracosm.sweets"
	}

	override fun addTranslations() {
		ModItemLang.add(this)
		ModBlockLang.add(this)
		ModEntityTypeLang.add(this)
		ModSubtitleLang.add(this)

		add(Curios.SEEING_STONE, "Seeing Stone")

		add(Emi.SEEING_STONE_TAG, "Seeing Stone")
		add(Emi.SWEETS_TAG, "Sweets")
	}

	companion object {
		fun String.toComponent(vararg args: Any): MutableComponent = Component.translatable(this, *args)
	}
}