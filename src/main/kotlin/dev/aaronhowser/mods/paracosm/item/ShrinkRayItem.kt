package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.abs

class ShrinkRayItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        private fun changeScale(
            entity: LivingEntity,
            scaleChange: Double,
            changer: Player? = null
        ): Boolean {
            val scaleBefore = entity.getAttributeValue(Attributes.SCALE)

            entity.shrinkRayEffect += scaleChange

            if (abs(entity.shrinkRayEffect) < 0.01) {
                entity.shrinkRayEffect = 0.0
            }

            val scaleAfter = entity.getAttributeValue(Attributes.SCALE)

            val scaleChanged = scaleBefore != scaleAfter

            if (scaleChanged) {
                entity.refreshDimensions()

                val entityName = entity.name.string
                val afterString = "%.2f".format(scaleAfter)
                val changeMessage = Component.literal("$entityName scale effect changed to $afterString")

                changer?.displayClientMessage(changeMessage, true)

                if (changer != entity && entity is Player) {
                    entity.displayClientMessage(changeMessage, true)
                }

            }

            return scaleChanged
        }

    }

    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        val changeAmount = if (player.isShiftKeyDown) -0.1 else 0.1

        val scaleChanged = changeScale(interactionTarget, changeAmount, player)

        return if (scaleChanged) {
            InteractionResult.sidedSuccess(interactionTarget.level().isClientSide)
        } else {
            InteractionResult.PASS
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)

        val changeAmount = if (player.isShiftKeyDown) -0.1 else 0.1

        val scaleChanged = changeScale(player, changeAmount)

        return if (scaleChanged) {
            InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
        } else {
            InteractionResultHolder.pass(usedStack)
        }
    }

}