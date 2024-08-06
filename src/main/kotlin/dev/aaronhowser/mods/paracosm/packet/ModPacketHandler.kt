package dev.aaronhowser.mods.paracosm.packet

import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateWhimsyValue
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object ModPacketHandler {

    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")

        registrar.playToClient(
            UpdateWhimsyValue.TYPE,
            UpdateWhimsyValue.STREAM_CODEC,
            DirectionalPayloadHandler(
                { packet, context -> packet.receiveMessage(context) },
                { packet, context -> packet.receiveMessage(context) }
            )
        )
    }


    fun messageNearbyPlayers(packet: ModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: ModPacket) {
        PacketDistributor.sendToPlayer(player, packet)
    }

    fun messageAllPlayers(packet: ModPacket) {
        PacketDistributor.sendToAllPlayers(packet)
    }

    fun messageServer(packet: ModPacket) {
        PacketDistributor.sendToServer(packet)
    }

}