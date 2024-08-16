package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.tags.EntityTypeTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, Paracosm.ID, existingFileHelper) {

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE)
            .add(ModEntityTypes.DODGEBALL.get())

    }

}