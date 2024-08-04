package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(Paracosm.ID)

    val COTTON: DeferredItem<Item> =
        ITEM_REGISTRY.registerItem("cotton") { ItemNameBlockItem(ModBlocks.COTTON.get(), Item.Properties()) }

}