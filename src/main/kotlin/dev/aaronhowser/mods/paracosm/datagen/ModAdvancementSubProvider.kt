package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
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

		fun guide(path: String) = OtherUtil.modResource("guide/$path")

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
			.save(saver, guide("root"), existingFileHelper)

	}

}