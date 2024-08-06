package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class SeeingStone : Item(
    Properties()
        .stacksTo(1)
) {

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.SPYGLASS
    }

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
        return 72000
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.whimsy += 10f

        return ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    override fun onStopUsing(stack: ItemStack, entity: LivingEntity, count: Int) {
        entity.whimsy -= 10f

        super.onStopUsing(stack, entity, count)
    }

}