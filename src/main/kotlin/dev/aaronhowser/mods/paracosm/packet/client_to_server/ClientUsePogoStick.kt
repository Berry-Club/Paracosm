package dev.aaronhowser.mods.paracosm.packet.client_to_server

import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvents
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientUsePogoStick private constructor() : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            val heldPogo = PogoStickItem.getHeldPogoStick(player) ?: return@enqueueWork

            player.playSound(SoundEvents.PISTON_EXTEND)

        }
    }

    override fun type(): CustomPacketPayload.Type<ClientUsePogoStick> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<ClientUsePogoStick> =
            CustomPacketPayload.Type(OtherUtil.modResource("client_use_pogo_stick"))

        val INSTANCE = ClientUsePogoStick()

        val STREAM_CODEC: StreamCodec<ByteBuf, ClientUsePogoStick> = StreamCodec.unit(INSTANCE)
    }
}