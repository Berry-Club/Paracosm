package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext

object ModResearchTypesProvider : RegistrySetBuilder() {

	fun bootstrap(context: BootstrapContext<ResearchType>) {

		context.register(
			ModResearchTypes.ACTION,
			ResearchType(0xFF5555)
		)

		context.register(
			ModResearchTypes.ADVENTURE,
			ResearchType(0x55FF55)
		)

		context.register(
			ModResearchTypes.COMFORT,
			ResearchType(0x5555FF)
		)

		context.register(
			ModResearchTypes.FRIENDSHIP,
			ResearchType(0xFFFF55)
		)

		context.register(
			ModResearchTypes.MOVEMENT,
			ResearchType(0x55FFFF)
		)

		context.register(
			ModResearchTypes.MISCHIEF,
			ResearchType(0xFF55FF)
		)

		context.register(
			ModResearchTypes.CONTRAPTIONS,
			ResearchType(0xAAAAAA)
		)

		context.register(
			ModResearchTypes.SPOOKY,
			ResearchType(0x000000)
		)

	}

}