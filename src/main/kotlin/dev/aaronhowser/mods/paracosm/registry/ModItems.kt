package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.*
import dev.aaronhowser.mods.paracosm.item.FancyFoodItem.Companion.fast
import net.minecraft.world.food.FoodProperties
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
    val DODGEBALL: DeferredItem<DodgeballItem> =
        ITEM_REGISTRY.registerItem("dodgeball") { DodgeballItem() }
    val SHRINK_RAY: DeferredItem<ShrinkRayItem> =
        ITEM_REGISTRY.registerItem("shrink_ray") { ShrinkRayItem() }
    val CANDY: DeferredItem<FancyFoodItem> =
        ITEM_REGISTRY.registerItem("candy") {
            FancyFoodItem(
                stacksTo = 99,
                isDrink = false,
                FoodProperties
                    .Builder()
                    .nutrition(1)
                    .saturationModifier(-0.1f)
                    .fast(0.1f)
                    .build()
            )
        }
    val SODA: DeferredItem<FancyFoodItem> =
        ITEM_REGISTRY.registerItem("soda") {
            FancyFoodItem(
                stacksTo = 1,
                isDrink = true,
                FoodProperties
                    .Builder()
                    .nutrition(1)
                    .saturationModifier(0.1f)
                    .build()
            )
        }

}