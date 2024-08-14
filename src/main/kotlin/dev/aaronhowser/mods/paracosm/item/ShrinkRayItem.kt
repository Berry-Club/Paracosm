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
        if (player.level().isClientSide || player.cooldowns.isOnCooldown(this)) {
            return InteractionResult.PASS
        }

        val scaleBefore = interactionTarget.getAttributeValue(Attributes.SCALE)

        if (player.isShiftKeyDown) {
            interactionTarget.shrinkRayEffect -= 0.1
        } else {
            interactionTarget.shrinkRayEffect += 0.1
        }

        val scaleAfter = interactionTarget.getAttributeValue(Attributes.SCALE)

        player.cooldowns.addCooldown(this, 1)

        return if (scaleBefore != scaleAfter) {
            InteractionResult.SUCCESS
        } else {
            InteractionResult.PASS
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)

        if (player.level().isClientSide || player.cooldowns.isOnCooldown(this)) {
            return InteractionResultHolder.pass(usedStack)
        }

        val scaleBefore = player.getAttributeValue(Attributes.SCALE)

        if (player.isSecondaryUseActive) {
            player.shrinkRayEffect -= 0.1
        } else {
            player.shrinkRayEffect += 0.1
        }

        val scaleAfter = player.getAttributeValue(Attributes.SCALE)

        player.cooldowns.addCooldown(this, 1)

        return if (scaleBefore != scaleAfter) {
            InteractionResultHolder.success(usedStack)
        } else {
            InteractionResultHolder.pass(usedStack)
        }
    }

}