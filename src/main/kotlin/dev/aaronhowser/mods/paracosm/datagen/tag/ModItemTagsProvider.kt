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

    companion object {
        private fun create(name: String): TagKey<Item> = TagKey.create(Registries.ITEM, OtherUtil.modResource(name))
        private fun create(rl: ResourceLocation): TagKey<Item> = TagKey.create(Registries.ITEM, rl)

        val SWEETS = create("sweets")
        val SEEING_STONE = create(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "seeing_stone"))
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(SWEETS)
            .add(
                Items.COOKIE
            )

        this.tag(SEEING_STONE)
            .add(
                ModItems.SEEING_STONE.get()
            )

    }
}