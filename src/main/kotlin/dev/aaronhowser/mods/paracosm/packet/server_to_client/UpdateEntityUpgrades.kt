package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.base.IUpgradeableEntity
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateEntityUpgrades(
	val entityId: Int,
	val upgrades: List<String>
) : AaronPacket() {

	override fun handleOnClient(context: IPayloadContext) {
		val entity = context.player().level().getEntity(entityId) ?: return

		IUpgradeableEntity.setUpgrades(entity, upgrades.toSet())
	}

	override fun type(): CustomPacketPayload.Type<UpdateEntityUpgrades> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdateEntityUpgrades> =
			CustomPacketPayload.Type(Paracosm.modResource("update_entity_upgrades"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdateEntityUpgrades> =
			StreamCodec.composite(
				ByteBufCodecs.INT, UpdateEntityUpgrades::entityId,
				ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), UpdateEntityUpgrades::upgrades,
				::UpdateEntityUpgrades
			)
	}

}