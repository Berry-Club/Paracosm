package dev.aaronhowser.mods.paracosm.datagen.language

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes

object ModSubtitleLang {

	const val FART = "subtitle.paracosm.fart"
	const val UNFART = "subtitle.paracosm.unfart"
	const val DODGEBALL = "subtitle.paracosm.dodgeball"
	const val SQUEE = "subtitle.paracosm.squee"
	const val STICKY_HAND_THROW = "subtitle.paracosm.sticky_hand_throw"
	const val STICKY_HAND_RETRIEVE = "subtitle.paracosm.sticky_hand_retrieve"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(FART, "Pbbbbt")
			add(UNFART, "Bbbtpp")
			add(DODGEBALL, "PHONK")
			add(SQUEE, "Squee")
			add(STICKY_HAND_THROW, "Throw Sticky Hand")
			add(STICKY_HAND_RETRIEVE, "Retrieve Sticky Hand")
		}
	}

}