package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.model.ModBlockStateProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(
    modid = Paracosm.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModDataGen {

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val generator: DataGenerator = event.generator
        val output: PackOutput = generator.packOutput
        val existingFileHelper: ExistingFileHelper = event.existingFileHelper
        val lookupProvider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

        val languageProvider = generator.addProvider(event.includeClient(), ModLanguageProvider(output))

        val blockStateProvider = generator.addProvider(
            event.includeClient(),
            ModBlockStateProvider(output, existingFileHelper)
        )
//
//        val itemModelProvider = generator.addProvider(
//            event.includeClient(),
//            ModItemModelProvider(output, existingFileHelper)
//        )
//
//        val recipeProvider = generator.addProvider(
//            event.includeServer(),
//            ModRecipeProvider(output, lookupProvider)
//        )
//
//        val blockTagProvider = generator.addProvider(
//            event.includeServer(),
//            ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
//        )
//
//        val itemTagProvider = generator.addProvider(
//            event.includeServer(),
//            ModItemTagsProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper)
//        )
//
//        val entityTypeTagProvider = generator.addProvider(
//            event.includeServer(),
//            ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
//        )
//
//        val blockLootTableProvider = generator.addProvider(
//            event.includeServer(),
//            LootTableProvider(
//                output,
//                setOf(),
//                listOf(
//                    LootTableProvider.SubProviderEntry(
//                        ::ModBlockLootTableSubProvider,
//                        LootContextParamSets.BLOCK
//                    )
//                ),
//                lookupProvider
//            )
//        )
//
//        val advancementProvider = generator.addProvider(
//            event.includeServer(),
//            AdvancementProvider(
//                output,
//                lookupProvider,
//                existingFileHelper,
//                listOf(ModAdvancementSubProvider())
//            )
//        )

    }

}