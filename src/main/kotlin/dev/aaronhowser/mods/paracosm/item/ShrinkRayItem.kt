package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.entity.custom.ShrinkRayProjectile
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ShrinkRayItem : RequiresWhimsy, Item(
    Properties()
        .stacksTo(1)
) {

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)

        //TODO: If no whimsy, make "pew" sound. If high whimsy, make laser sound

        if (!hasEnoughWhimsy(player)) {
            return InteractionResultHolder.fail(usedStack)
        }

        if (player is ServerPlayer) {
            val shrinkRayProjectile = ShrinkRayProjectile(player, player.isSecondaryUseActive)

            shrinkRayProjectile.shootFromRotation(
                player,
                player.xRot,
                player.yRot,
                0.0F,
                1.5F,
                1.0F
            )

            level.addFreshEntity(shrinkRayProjectile)
        }

        return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
    }

    override val requiredWhimsy: Float = 10f

}