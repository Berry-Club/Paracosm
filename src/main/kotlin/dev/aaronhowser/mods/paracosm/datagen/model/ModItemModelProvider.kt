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

        val makeModelsFor = listOf(
            ModItems.COTTON,
            ModItems.TOWEL_CAPE
        )

        for (item in makeModelsFor) {
            basicItem(item.get())
        }

    }
}