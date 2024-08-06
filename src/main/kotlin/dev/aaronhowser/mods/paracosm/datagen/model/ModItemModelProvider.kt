package dev.aaronhowser.mods.paracosm.datagen.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemNameBlockItem
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, Paracosm.ID, existingFileHelper) {

    override fun registerModels() {
        for (deferred in ModItems.ITEM_REGISTRY.entries) {
            val item = deferred.get()
            if (item is BlockItem && item !is ItemNameBlockItem) continue
            if (item is RequiresWhimsy && item.hasCustomModelHandling) continue

            basicItem(item)
        }

    }
}