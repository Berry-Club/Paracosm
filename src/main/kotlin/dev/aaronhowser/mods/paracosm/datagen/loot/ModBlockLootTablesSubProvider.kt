package dev.aaronhowser.mods.paracosm.datagen.loot

import dev.aaronhowser.mods.paracosm.block.CottonBlock
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	override fun generate() {
		dropSelf(ModBlocks.NIGHT_LIGHT.get())
		dropSelf(ModBlocks.WALRUS.get())
		dropSelf(ModBlocks.WHOOPEE_CUSHION.get())
		dropSelf(ModBlocks.IMAGINATOR.get())
		dropSelf(ModBlocks.CITY_RUG.get())

		cotton()
	}

	private fun cotton() {
		val block = ModBlocks.COTTON.get()
		val builder = LootItemBlockStatePropertyCondition
			.hasBlockStateProperties(block)
			.setProperties(
				StatePropertiesPredicate
					.Builder
					.properties()
					.hasProperty(CottonBlock.AGE_3, 3)
			)

		add(
			block,
			createCropDrops(
				block,
				ModItems.COTTON.get(),
				ModItems.COTTON.get(),
				builder
			)
		)

	}

	override fun getKnownBlocks(): List<Block> {
		return listOf(
			ModBlocks.NIGHT_LIGHT.get(),
			ModBlocks.COTTON.get(),
			ModBlocks.WALRUS.get(),
			ModBlocks.WHOOPEE_CUSHION.get(),
			ModBlocks.IMAGINATOR.get(),
			ModBlocks.CITY_RUG.get(),
		)
	}

}