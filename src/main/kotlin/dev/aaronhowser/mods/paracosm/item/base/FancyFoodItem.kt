package dev.aaronhowser.mods.paracosm.item.base

import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim

class FancyFoodItem(
	private val isDrink: Boolean,
	properties: Properties,
) : Item(properties) {

	override fun getUseAnimation(stack: ItemStack): UseAnim {
		return if (isDrink) UseAnim.DRINK else UseAnim.EAT
	}

	companion object {
		val DEFAULT_CANDY_PROPERTIES = {
			Properties()
				.stacksTo(99)
				.food(
					FoodProperties
						.Builder()
						.nutrition(1)
						.saturationModifier(-0.1f)
						.eatTime(0.1f)
						.build()
				)
		}

		val DEFAULT_SODA_PROPERTIES = {
			Properties()
				.stacksTo(1)
				.food(
					FoodProperties
						.Builder()
						.nutrition(1)
						.saturationModifier(0.1f)
						.build()
				)
		}

		fun FoodProperties.Builder.eatTime(seconds: Float): FoodProperties.Builder {
			this.eatSeconds = seconds
			return this
		}
	}

}