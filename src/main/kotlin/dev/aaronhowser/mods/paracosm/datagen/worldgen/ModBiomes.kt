package dev.aaronhowser.mods.paracosm.datagen.worldgen

import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.*

object ModBiomes {

    val TEST_BIOME_ID: ResourceLocation = OtherUtil.modResource("test_biome")
    val TEST_BIOME_KEY: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME, TEST_BIOME_ID)

    fun boostrap(context: BootstrapContext<Biome>) {
        context.register(TEST_BIOME_KEY, testBiome(context))
    }

    private fun testBiome(context: BootstrapContext<Biome>): Biome {

        val spawnBuilder = MobSpawnSettings.Builder()
        spawnBuilder.addSpawn(
            MobCategory.CREATURE,
            MobSpawnSettings.SpawnerData(
                ModEntityTypes.AARONBERRY.get(),
                2,
                3,
                5
            )
        )

        spawnBuilder.addSpawn(
            MobCategory.CREATURE,
            MobSpawnSettings.SpawnerData(
                ModEntityTypes.TEDDY_BEAR.get(),
                2,
                3,
                5
            )
        )

        spawnBuilder.addSpawn(
            MobCategory.MISC,
            MobSpawnSettings.SpawnerData(
                EntityType.SNOW_GOLEM,
                2,
                3,
                5
            )
        )

        val biomeBuilder = BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER)
        )

        BiomeDefaultFeatures.addDefaultOres(biomeBuilder)
        BiomeDefaultFeatures.addExtraGold(biomeBuilder)
        BiomeDefaultFeatures.addExtraEmeralds(biomeBuilder)
        BiomeDefaultFeatures.addCommonBerryBushes(biomeBuilder)
        BiomeDefaultFeatures.addTallBirchTrees(biomeBuilder)

        val specialEffects = BiomeSpecialEffects.Builder()
            .waterColor(0x3939C9)
            .waterFogColor(0x05006B)
            .fogColor(0xC0D8DF)
            .skyColor(0x30c918)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .build()

        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .downfall(0f)
            .temperature(0.4f)
            .generationSettings(biomeBuilder.build())
            .mobSpawnSettings(spawnBuilder.build())
            .specialEffects(specialEffects)
            .build()
    }

}