package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
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

		val root = advancement()
			.display(
				ModItems.COTTON.get(),
				Component.literal("Paracosm"),
				Component.literal("Yay!! Haha yes yeah haha"),
				Paracosm.modResource("textures/block/city_rug0.png"),
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

		advancement()
			.parent(root)
			.display(
				ModBlocks.NIGHT_LIGHT.get(),
				Component.literal("Sleep with Night Light"),
				Component.literal("Sleep in the dark with an active Night Light nearby")
			)
			.addImpossibleCriterion()
			.save(saver, SLEEP_WITH_NIGHT_LIGHT, existingFileHelper)

	}

	companion object {
		val ROOT = guide("root")
		val SLEEP_WITH_NIGHT_LIGHT = guide("sleep_with_night_light")

		private fun advancement(): Advancement.Builder = Advancement.Builder.advancement()
		private fun guide(path: String): ResourceLocation = Paracosm.modResource("guide/$path")

		private fun Advancement.Builder.addImpossibleCriterion(): Advancement.Builder {
			return addCriterion(
				"impossible",
				CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
			)
		}

		private fun Advancement.Builder.display(
			icon: ItemLike,
			title: Component,
			description: Component,
			type: AdvancementType = AdvancementType.TASK,
			showToast: Boolean = true,
			announceToChat: Boolean = true,
			hidden: Boolean = false
		): Advancement.Builder {
			return display(
				icon,
				title,
				description,
				null,
				type,
				showToast,
				announceToChat,
				hidden
			)
		}
	}

}