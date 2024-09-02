package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.item.pogo_stick.BounceHandler
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.*

class TellClientsUsedPogo(
    val uuidMost: Long,
    val uuidLeast: Long,
) : ModPacket {

    constructor(player: Player) : this(player.uuid.mostSignificantBits, player.uuid.leastSignificantBits)

    private fun getUuid(): UUID {
        return UUID(uuidMost, uuidLeast)
    }

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player().level().getPlayerByUUID(getUuid()) ?: return@enqueueWork

            BounceHandler.addBouncer(player, 0.0)
        }
    }

    override fun type(): CustomPacketPayload.Type<TellClientsUsedPogo> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<TellClientsUsedPogo> =
            CustomPacketPayload.Type(OtherUtil.modResource("tell_clients_used_pogo"))

        val STREAM_CODEC: StreamCodec<ByteBuf, TellClientsUsedPogo> = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, TellClientsUsedPogo::uuidMost,
            ByteBufCodecs.VAR_LONG, TellClientsUsedPogo::uuidLeast,
            ::TellClientsUsedPogo
        )

    }
}