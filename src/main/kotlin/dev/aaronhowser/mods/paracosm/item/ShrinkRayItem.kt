package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ShrinkRayItem : Item(
    Properties()
        .stacksTo(1)
) {

    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        if (player.level().isClientSide) return InteractionResult.PASS

        val scaleBefore = interactionTarget.getAttributeValue(Attributes.SCALE)

        if (player.isShiftKeyDown) {
            interactionTarget.shrinkRayEffect -= 0.1
        } else {
            interactionTarget.shrinkRayEffect += 0.1
        }

        val scaleAfter = interactionTarget.getAttributeValue(Attributes.SCALE)

        return if (scaleBefore != scaleAfter) {
            InteractionResult.CONSUME
        } else {
            InteractionResult.PASS
        }
    }

    //FIXME: Still procs when you click an entity
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)

        if (player.level().isClientSide) return InteractionResultHolder.pass(usedStack)

        if (player.isSecondaryUseActive) {
            player.shrinkRayEffect -= 0.1
        } else {
            player.shrinkRayEffect += 0.1
        }

        return InteractionResultHolder.pass(usedStack)
    }

}