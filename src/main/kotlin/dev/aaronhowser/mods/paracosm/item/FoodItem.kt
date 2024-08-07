package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item

class FoodItem(
    stacksTo: Int,
    foodProperties: FoodProperties
) : Item(
    Properties()
        .stacksTo(stacksTo)
        .food(foodProperties)
) {

    companion object {
        fun FoodProperties.Builder.fast(seconds: Float): FoodProperties.Builder {
            this.eatSeconds = seconds
            return this
        }
    }

}