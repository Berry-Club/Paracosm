package dev.aaronhowser.mods.paracosm.datagen.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, Paracosm.ID, existingFileHelper) {

    override fun registerModels() {
        val basicModels = listOf(
            ModItems.COTTON,
            ModItems.TOWEL_CAPE,
            ModItems.CANDY,
            ModItems.SODA,
            ModItems.WARM_MILK,
            ModItems.SHRINK_RAY, //TODO: Make whimsy
        )

        for (item in basicModels) {
            basicItem(item.get())
        }

        whimsy(
            ModItems.TOY_GUN,
            mcLoc("item/stick"),
            mcLoc("item/diamond")
        )

    }

    companion object {
        val whimsyPredicateName = OtherUtil.modResource("whimsy")
    }

    fun whimsy(
        item: ItemLike,
        textureOne: ResourceLocation,
        textureTwo: ResourceLocation
    ) = whimsy(item.asItem(), textureOne, textureTwo)

    fun whimsy(
        item: Item,
        textureDefault: ResourceLocation,
        textureWhimsy: ResourceLocation
    ) {
        val name = BuiltInRegistries.ITEM.getKey(item)

        val whimsyModel = getBuilder(name.toString() + "_whimsy")
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", textureWhimsy)

        getBuilder(name.toString())
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", textureDefault)
            .override()
            .predicate(
                modLoc("whimsy"),
                1.0f
            )
            .model(whimsyModel)
            .end()
    }

}