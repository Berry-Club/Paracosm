package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.UUID

class ServerUsePogoStick(
    val playerUUID: UUID
) : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        TODO("Not yet implemented")
    }

    override fun type(): CustomPacketPayload.Type<ServerUsePogoStick> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<ServerUsePogoStick> =
            CustomPacketPayload.Type(OtherUtil.modResource("server_use_pogo_stick"))

//        val STREAM_CODEC: StreamCodec<ByteBuf, ServerUsePogoStick> = StreamCodec.composite(
//
//        )

    }
}