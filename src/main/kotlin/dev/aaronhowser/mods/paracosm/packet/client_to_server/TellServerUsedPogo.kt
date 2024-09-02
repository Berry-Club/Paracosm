package dev.aaronhowser.mods.paracosm.packet.client_to_server

import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class TellServerUsedPogo private constructor() : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player() as? ServerPlayer ?: return@enqueueWork

            PogoStickItem.usePogo(player)
        }
    }

    override fun type(): CustomPacketPayload.Type<TellServerUsedPogo> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<TellServerUsedPogo> =
            CustomPacketPayload.Type(OtherUtil.modResource("tell_server_used_pogo"))

        val INSTANCE = TellServerUsedPogo()

        val STREAM_CODEC: StreamCodec<ByteBuf, TellServerUsedPogo> = StreamCodec.unit(INSTANCE)
    }
}