package dev.aaronhowser.mods.paracosm.datagen.worldgen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import java.util.*

object ModDimensions {

	val DREAM_DIMENSION_ID: ResourceLocation =
		Paracosm.modResource("dream")
	val DREAM_DIMENSION_TYPE: ResourceKey<DimensionType> =
		ResourceKey.create(Registries.DIMENSION_TYPE, DREAM_DIMENSION_ID)
	val DREAM_DIMENSION_LEVEL: ResourceKey<Level> =
		ResourceKey.create(Registries.DIMENSION, DREAM_DIMENSION_ID)
	val DREAM_DIMENSION_LEVEL_STEM: ResourceKey<LevelStem> =
		ResourceKey.create(Registries.LEVEL_STEM, DREAM_DIMENSION_ID)

	fun bootstrapDimensionType(context: BootstrapContext<DimensionType>) {
		context.register(
			DREAM_DIMENSION_TYPE, DimensionType(
				OptionalLong.empty(),
				true,
				false,
				false,
				true,
				1.0,
				true,
				true,
				0,
				256,
				256,
				BlockTags.INFINIBURN_OVERWORLD,
				Paracosm.modResource("dream"),
				0f,
				DimensionType.MonsterSettings(
					false,
					false,
					UniformInt.of(0, 7),
					0
				)
			)
		)
	}

	fun bootstrapLevelStem(context: BootstrapContext<LevelStem>) {

		val biomeRegistry = context.lookup(Registries.BIOME)
		val dimTypeRegistry = context.lookup(Registries.DIMENSION_TYPE)
		val noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS)

		val singleBiomeGen = NoiseBasedChunkGenerator(
			FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME_KEY)),
			noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
		)

		context.register(
			DREAM_DIMENSION_LEVEL_STEM,
			LevelStem(
				dimTypeRegistry.getOrThrow(DREAM_DIMENSION_TYPE),
				singleBiomeGen
			)
		)

	}


}