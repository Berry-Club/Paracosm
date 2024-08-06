package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.attachment.Delusion.Companion.delusion
import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateWhimsyValue(
    val entityId: Int,
    val newAmount: Float,
    val isWhimsy: Boolean
) : ModPacket {

    val isDelusion = !isWhimsy

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val entity = context.player().level().getEntity(entityId) as? LivingEntity ?: return@enqueueWork

            if (isWhimsy) {
                entity.whimsy = newAmount
            } else {
                entity.delusion = newAmount
            }
        }
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<UpdateWhimsyValue> =
            CustomPacketPayload.Type(OtherUtil.modResource("update_whimsy_value"))

        val STREAM_CODEC: StreamCodec<ByteBuf, UpdateWhimsyValue> =
            StreamCodec.composite(
                ByteBufCodecs.INT, UpdateWhimsyValue::entityId,
                ByteBufCodecs.FLOAT, UpdateWhimsyValue::newAmount,
                ByteBufCodecs.BOOL, UpdateWhimsyValue::isWhimsy,
                ::UpdateWhimsyValue
            )
    }

}