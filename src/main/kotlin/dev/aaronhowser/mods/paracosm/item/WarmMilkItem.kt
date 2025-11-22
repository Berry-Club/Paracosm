package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.MilkBucketItem
import net.minecraft.world.level.Level

class WarmMilkItem(properties: Properties) : MilkBucketItem(properties) {

	override fun finishUsingItem(stack: ItemStack, level: Level, entityLiving: LivingEntity): ItemStack {
		println("Warm milk was finished using!")

		return super.finishUsingItem(stack, level, entityLiving)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.stacksTo(1)
				.craftRemainder(Items.BUCKET)
	}

}