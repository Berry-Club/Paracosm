package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.TellClientsUsedPogo
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.AABB

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

        fun usePogo(player: ServerPlayer) {
            val heldPogo = getHeldPogoStick(player) ?: return

            player.playSound(SoundEvents.PISTON_EXTEND)

            ModPacketHandler.messageNearbyPlayers(
                TellClientsUsedPogo(player),
                player.level() as ServerLevel,
                player.eyePosition,
                64.0
            )

            var damageItemAmount = 0

            //TODO: Configurable, upgradeable?
            val damage = minOf(player.fallDistance, 6f)

            if (player.fallDistance >= 2f) {

                val box = AABB(
                    player.position().x - 0.3,
                    player.position().y - 0.0,
                    player.position().z - 0.3,
                    player.position().x + 0.3,
                    player.position().y + 0.6,
                    player.position().z + 0.3
                )

                for (entity in player.level().getEntities(player, box)) {
                    if (entity == player) continue

                    entity.hurt(player.level().damageSources().flyIntoWall(), damage)
                    damageItemAmount++
                }
            }

            val shouldFallDamageItem =
                !player.isCreative
                        && player.fallDistance > 1f
                        && !player.level()
                    .getBlockState(player.blockPosition().below())
                    .`is`(ModBlockTagsProvider.POGO_BOOST)

            if (shouldFallDamageItem) damageItemAmount++

            if (damageItemAmount > 0) {
                val equipmentSlot = player.getEquipmentSlotForItem(heldPogo)
                heldPogo.hurtAndBreak(damageItemAmount, player, equipmentSlot)
            }

            player.fallDistance = 0f
        }

    }

}