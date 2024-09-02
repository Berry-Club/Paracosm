package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class PogoStickItem(
    properties: Properties = Properties()
        .durability(256)
) : Item(properties) {

    companion object {
        fun getHeldPogoStick(player: Player): ItemStack? {
            val mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND)
            if (mainHandStack.item == ModItems.POGO_STICK.get()) {
                return mainHandStack
            }

            val offHandStack = player.getItemInHand(InteractionHand.OFF_HAND)
            if (offHandStack.item == ModItems.POGO_STICK.get()) {
                return offHandStack
            }

            return null
        }

    }

}