package dev.aaronhowser.mods.paracosm.datagen.language

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes

object ModEntityTypeLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addEntityType(ModEntityTypes.TEDDY_BEAR, "Teddy Bear")
			addEntityType(ModEntityTypes.DODGEBALL, "Dodgeball")
			addEntityType(ModEntityTypes.STRING_WORM, "String Worm")
			addEntityType(ModEntityTypes.AARONBERRY, "Aaronberry")
		}
	}

}