package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.Delusion.Companion.delusion
import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import dev.aaronhowser.mods.paracosm.command.ModCommands
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateWhimsyValue
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.LivingFallEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent

@EventBusSubscriber(
    modid = Paracosm.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

    @SubscribeEvent
    fun afterUseItem(event: LivingEntityUseItemEvent.Finish) {
        val entity = event.entity

        val food = event.item.getFoodProperties(entity)
        if (food != null) {

            if (event.item.`is`(ModItemTagsProvider.SWEETS)) {
                entity.whimsy += 0.5f
            }

        }
    }

    @SubscribeEvent
    fun onStartTracking(event: PlayerEvent.StartTracking) {
        val player = event.entity as? ServerPlayer ?: return
        val entity = event.target as? LivingEntity ?: return

        ModPacketHandler.messagePlayer(
            player,
            UpdateWhimsyValue(
                entity.id,
                entity.whimsy,
                true
            )
        )

        ModPacketHandler.messagePlayer(
            player,
            UpdateWhimsyValue(
                entity.id,
                entity.delusion,
                false
            )
        )
    }

    @SubscribeEvent
    fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return

        ModPacketHandler.messagePlayer(
            player,
            UpdateWhimsyValue(
                player.id,
                player.whimsy,
                true
            )
        )

        ModPacketHandler.messagePlayer(
            player,
            UpdateWhimsyValue(
                player.id,
                player.delusion,
                false
            )
        )
    }

    @SubscribeEvent
    fun onLivingFall(event: LivingFallEvent) {
        PogoStickItem.onPlayerLand(event)
    }

}