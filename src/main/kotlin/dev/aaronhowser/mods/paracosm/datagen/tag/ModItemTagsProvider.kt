package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosApi
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
	pOutput: PackOutput,
	pLookupProvider: CompletableFuture<HolderLookup.Provider>,
	pBlockTags: CompletableFuture<TagLookup<Block>>,
	existingFileHelper: ExistingFileHelper?
) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, Paracosm.ID, existingFileHelper) {

	override fun addTags(pProvider: HolderLookup.Provider) {

		this.tag(SWEETS)
			.add(
				Items.COOKIE,
				ModItems.CANDY.get(),
				ModItems.SODA.get()
			)

		this.tag(SEEING_STONE)
			.add(
				ModItems.SEEING_STONE.get()
			)

	}

	companion object {
		private fun create(name: String): TagKey<Item> {
			val rl = OtherUtil.modResource(name)
			return TagKey.create(Registries.ITEM, rl)
		}

		private fun curio(name: String): TagKey<Item> {
			val rl = ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, name)
			return TagKey.create(Registries.ITEM, rl)
		}

		val SWEETS = create("sweets")
		val SEEING_STONE = curio("seeing_stone")
	}
}