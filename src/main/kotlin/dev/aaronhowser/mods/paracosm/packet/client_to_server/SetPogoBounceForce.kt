package dev.aaronhowser.mods.paracosm.packet.client_to_server

import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class SetPogoBounceForce(
    val force: Double
) : ModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()

            val delta = player.deltaMovement
            player.setDeltaMovement(
                delta.x,
                force,
                delta.z
            )
        }
    }

    override fun type(): CustomPacketPayload.Type<SetPogoBounceForce> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<SetPogoBounceForce> =
            CustomPacketPayload.Type(OtherUtil.modResource("set_pogo_bounce_force"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SetPogoBounceForce> =
            StreamCodec.composite(
                ByteBufCodecs.DOUBLE, SetPogoBounceForce::force,
                ::SetPogoBounceForce
            )
    }

}