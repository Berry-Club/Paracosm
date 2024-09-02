package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.event.entity.living.LivingFallEvent
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent

class PogoStickItem(
    properties: Properties = Properties()
        .durability(256)
) : Item(properties) {

    companion object {

        fun handleEvent(event: LivingFallEvent) {
            val player = event.entity as? Player ?: return

            if (handlePogoLand(player, event.distance)) {
                event.isCanceled = true
            }
        }

        fun handleEvent(event: PlayerFlyableFallEvent) {
            val player = event.entity
            handlePogoLand(player, event.distance)
        }

        fun handlePogoLand(player: Player, distance: Float): Boolean {
            if (distance < 0.5f) return false
            if (player.isSuppressingBounce) return false

            val pogoStickStack = getHeldPogoStick(player) ?: return false

            player.hurtMarked = true

            val delta = player.deltaMovement
            val newY = minOf(delta.y * -10, 1000000000.0)

            player.setDeltaMovement(
                delta.x,
                newY,
                delta.z
            )

            val equipmentSlot = player.getEquipmentSlotForItem(pogoStickStack)
            pogoStickStack.hurtAndBreak(1, player, equipmentSlot)

            return true
        }

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