package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.paracosm.datagen.model.ModBlockStateProvider
import dev.aaronhowser.mods.paracosm.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = Paracosm.ID)
object ModDataGen {

	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) {
		val generator: DataGenerator = event.generator
		val output: PackOutput = generator.packOutput
		val existingFileHelper: ExistingFileHelper = event.existingFileHelper
		val lookupProvider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

		generator.addProvider(event.includeClient(), ModLanguageProvider(output))
		generator.addProvider(
			event.includeClient(),
			ModItemModelProvider(output, existingFileHelper)
		)
		generator.addProvider(
			event.includeClient(),
			ModBlockStateProvider(output, existingFileHelper)
		)

//        val recipeProvider = generator.addProvider(event.includeServer(), ModRecipeProvider(output, lookupProvider))

		val blockTagProvider = generator.addProvider(
			event.includeServer(),
			ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModItemTagsProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)
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

		generator.addProvider(
			event.includeServer(),
			ModLootTableProvider.create(output, lookupProvider)
		)

		generator.addProvider(
			event.includeClient(),
			ModSoundDefinitionsProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModDatapackBuiltinEntriesProvider(output, lookupProvider)
		)

		generator.addProvider(
			event.includeServer(),
			ModCurioProvider(output, existingFileHelper, lookupProvider)
		)

	}

}