package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, Paracosm.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            ModBlocks.NIGHT_LIGHT.get()
        )

        this.tag(BlockTags.CROPS).add(ModBlocks.COTTON.get())
        this.tag(BlockTags.PRESSURE_PLATES).add(ModBlocks.WHOOPEE_CUSHION.get())

    }
}