package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	val lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AdvancementProvider.AdvancementGenerator {

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>,
		existingFileHelper: ExistingFileHelper
	) {

		val root = Advancement.Builder.advancement()
			.display(
				ModItems.COTTON.get(),
				Component.literal("Paracosm"),
				Component.literal("Yay!! Haha yes yeah haha"),
				OtherUtil.modResource("textures/block/city_rug0.png"),
				AdvancementType.TASK,
				true,
				true,
				false
			)
			.addCriterion(
				"has_cotton",
				InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COTTON)
			)
			.save(saver, ROOT, existingFileHelper)

		Advancement.Builder.advancement()
			.parent(root)
			.display(
				ModBlocks.NIGHT_LIGHT.get(),
				Component.literal("Sleep with Night Light"),
				Component.literal("Sleep in the dark with an active Night Light nearby"),
				null,
				AdvancementType.TASK,
				true, true, false
			)
			.addImpossibleCriterion()
			.rewards(
				AdvancementRewards.Builder.function(ModMcFunctionProvider.GIVE_ONE_COMFORT)
			)
			.save(saver, SLEEP_WITH_NIGHT_LIGHT, existingFileHelper)

	}

	companion object {
		private fun guide(path: String) = OtherUtil.modResource("guide/$path")

		private fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
			return addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
		}

		val ROOT = guide("root")
		val SLEEP_WITH_NIGHT_LIGHT = guide("sleep_with_night_light")
	}

}