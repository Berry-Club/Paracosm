package dev.aaronhowser.mods.paracosm.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class StickyHandItem : Item(
    Properties()
        .stacksTo(1)
) {

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val heldItem = player.getItemInHand(usedHand)



        return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide)
    }

}