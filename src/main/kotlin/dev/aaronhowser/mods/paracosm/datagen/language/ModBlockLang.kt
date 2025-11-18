package dev.aaronhowser.mods.paracosm.datagen.language

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider
import dev.aaronhowser.mods.paracosm.registry.ModBlocks

object ModBlockLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addBlock(ModBlocks.NIGHT_LIGHT, "Night Light")
			addBlock(ModBlocks.WHOOPEE_CUSHION, "Whoopee Cushion")
			addBlock(ModBlocks.COTTON, "Cotton")
			addBlock(ModBlocks.WALRUS, "Walrus")
			addBlock(ModBlocks.IMAGINATOR, "Imaginator")
			addBlock(ModBlocks.CITY_RUG, "City Rug")
		}
	}

}