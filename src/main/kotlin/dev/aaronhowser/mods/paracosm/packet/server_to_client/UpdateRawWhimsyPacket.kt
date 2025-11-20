package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.handler.DelusionHandler.rawDelusion
import dev.aaronhowser.mods.paracosm.handler.WhimsyHandler.rawWhimsy
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateRawWhimsyPacket(
	val entityId: Int,
	val newAmount: Double,
	val isWhimsy: Boolean
) : ModPacket {

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun receiveMessage(context: IPayloadContext) {
		context.enqueueWork {
			// Return if not near player
			val entity = context.player().level().getEntity(entityId) as? LivingEntity ?: return@enqueueWork

			if (isWhimsy) {
				entity.rawWhimsy = newAmount
			} else {
				entity.rawDelusion = newAmount
			}
		}
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdateRawWhimsyPacket> =
			CustomPacketPayload.Type(OtherUtil.modResource("update_whimsy"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdateRawWhimsyPacket> =
			StreamCodec.composite(
				ByteBufCodecs.INT, UpdateRawWhimsyPacket::entityId,
				ByteBufCodecs.DOUBLE, UpdateRawWhimsyPacket::newAmount,
				ByteBufCodecs.BOOL, UpdateRawWhimsyPacket::isWhimsy,
				::UpdateRawWhimsyPacket
			)
	}

}