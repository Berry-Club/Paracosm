package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateShrinkRayScale(
    val entityId: Int,
    val newScale: Double
) : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val entity = context.player().level().getEntity(entityId) as? LivingEntity ?: return@enqueueWork

            entity.shrinkRayEffect = newScale
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<UpdateShrinkRayScale> =
            CustomPacketPayload.Type(OtherUtil.modResource("update_shrink_ray_scale"))

        val STREAM_CODEC: StreamCodec<ByteBuf, UpdateShrinkRayScale> =
            StreamCodec.composite(
                ByteBufCodecs.INT, UpdateShrinkRayScale::entityId,
                ByteBufCodecs.DOUBLE, UpdateShrinkRayScale::newScale,
                ::UpdateShrinkRayScale
            )
    }


}