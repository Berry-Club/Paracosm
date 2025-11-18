package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, Paracosm.ID, existingFileHelper) {

	companion object {
		fun create(name: String): TagKey<Block> = create(OtherUtil.modResource(name))
		fun create(rl: ResourceLocation): TagKey<Block> = TagKey.create(Registries.BLOCK, rl)

		val REFLECTIVE = create("reflective")
		val POGO_BOOST = create("pogo_boost")
	}

	override fun addTags(pProvider: HolderLookup.Provider) {

		this.tag(REFLECTIVE)
			.addTags(Tags.Blocks.GLASS_BLOCKS)
			.addTags(Tags.Blocks.GLAZED_TERRACOTTAS)

		this.tag(POGO_BOOST)
			.add(Blocks.SLIME_BLOCK)
			.addTags(BlockTags.BEDS)

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(ModBlocks.NIGHT_LIGHT.get())

		this.tag(BlockTags.MINEABLE_WITH_AXE)
			.add(ModBlocks.IMAGINATOR.get())

		this.tag(BlockTags.CROPS).add(ModBlocks.COTTON.get())
		this.tag(BlockTags.PRESSURE_PLATES).add(ModBlocks.WHOOPEE_CUSHION.get())

	}
}