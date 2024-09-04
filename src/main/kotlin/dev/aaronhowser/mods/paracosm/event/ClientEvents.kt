package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.client_to_server.UpdatePogoControls
import net.minecraft.client.player.LocalPlayer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(
    modid = "paracosm",
    value = [Dist.CLIENT]
)
object ClientEvents {

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        val player = event.entity as? LocalPlayer ?: return

        val vehicle = player.vehicle
        if (vehicle is PogoStickVehicle) {
            ModPacketHandler.messageServer(
                UpdatePogoControls(
                    player.input.leftImpulse,
                    player.input.forwardImpulse,
                    player.input.jumping
                )
            )
        }
    }

}