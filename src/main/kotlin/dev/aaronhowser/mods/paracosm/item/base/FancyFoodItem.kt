package dev.aaronhowser.mods.paracosm.item.base

import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim

class FancyFoodItem(
    stacksTo: Int,
    private val isDrink: Boolean,
    foodProperties: FoodProperties
) : Item(
    Properties()
        .stacksTo(stacksTo)
        .food(foodProperties)
) {

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return if (isDrink) UseAnim.DRINK else UseAnim.EAT
    }

    companion object {
        fun FoodProperties.Builder.fast(seconds: Float): FoodProperties.Builder {
            this.eatSeconds = seconds
            return this
        }
    }

}