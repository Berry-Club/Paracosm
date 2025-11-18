package dev.aaronhowser.mods.paracosm.packet.client_to_server

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdatePogoControls(
	val leftImpulse: Float,
	val forwardImpulse: Float,
	val jumping: Boolean
) : ModPacket {

	override fun receiveMessage(context: IPayloadContext) {
		context.enqueueWork {
			val player = context.player()
			val pogoEntity = player.controlledVehicle as? PogoStickVehicle ?: return@enqueueWork

			pogoEntity.setInput(leftImpulse, forwardImpulse, jumping)
		}
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdatePogoControls> =
			CustomPacketPayload.Type(OtherUtil.modResource("update_pogo_controls"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdatePogoControls> = StreamCodec.composite(
			ByteBufCodecs.FLOAT, UpdatePogoControls::leftImpulse,
			ByteBufCodecs.FLOAT, UpdatePogoControls::forwardImpulse,
			ByteBufCodecs.BOOL, UpdatePogoControls::jumping,
			::UpdatePogoControls
		)
	}

}