package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.baseWhimsy
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

class SeeingStone : ICurioItem, Item(
	Properties()
		.stacksTo(1)
) {

	override fun getUseAnimation(stack: ItemStack): UseAnim {
		return UseAnim.SPYGLASS
	}

	override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
		return 72000
	}

	//TODO: Make this an attribute modifier
	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		if (!level.isClientSide) player.baseWhimsy += 10f

		return ItemUtils.startUsingInstantly(level, player, usedHand)
	}

	override fun onStopUsing(stack: ItemStack, entity: LivingEntity, count: Int) {
		if (entity.isServerSide) entity.baseWhimsy -= 10f

		super.onStopUsing(stack, entity, count)
	}

	override fun curioTick(slotContext: SlotContext?, stack: ItemStack?) {
//        println("Curio tick")
	}

}