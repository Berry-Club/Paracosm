package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.worldgen.ModBiomes
import dev.aaronhowser.mods.paracosm.datagen.worldgen.ModDimensions
import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class ModDatapackBuiltinEntriesProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
	output,
	registries,
	BUILDER,
	setOf(Paracosm.MOD_ID)
) {

	companion object {
		val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
			.add(Registries.BIOME, ModBiomes::bootstrap)
			.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapDimensionType)
			.add(Registries.LEVEL_STEM, ModDimensions::bootstrapLevelStem)
			.add(ModResearchTypes.RESEARCH_TYPE_RK, ModResearchTypesProvider::bootstrap)
	}

}