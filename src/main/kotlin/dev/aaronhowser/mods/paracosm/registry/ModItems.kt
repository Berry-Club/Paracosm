package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.SeeingStone
import dev.aaronhowser.mods.paracosm.item.TowelCapeItem
import dev.aaronhowser.mods.paracosm.item.ToyGunItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(Paracosm.ID)

    val COTTON: DeferredItem<Item> =
        ITEM_REGISTRY.registerItem("cotton") { ItemNameBlockItem(ModBlocks.COTTON.get(), Item.Properties()) }
    val TOY_GUN: DeferredItem<ToyGunItem> =
        ITEM_REGISTRY.registerItem("toy_gun") { ToyGunItem() }
    val TOWEL_CAPE: DeferredItem<TowelCapeItem> =
        ITEM_REGISTRY.registerItem("towel_cape") { TowelCapeItem() }
    val SEEING_STONE: DeferredItem<SeeingStone> =
        ITEM_REGISTRY.registerItem("seeing_stone") { SeeingStone() }

}