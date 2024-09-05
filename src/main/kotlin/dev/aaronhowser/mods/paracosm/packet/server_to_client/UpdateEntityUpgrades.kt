package dev.aaronhowser.mods.paracosm.packet.server_to_client

import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateEntityUpgrades(
    val entityId: Int,
    val upgrades: List<String>
) : ModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val entity = context.player().level().getEntity(entityId) ?: return@enqueueWork

            Upgradeable.setUpgrades(entity, upgrades.toSet())
        }
    }

    override fun type(): CustomPacketPayload.Type<UpdateEntityUpgrades> {
        return TYPE
    }

    companion object {
        val TYPE: CustomPacketPayload.Type<UpdateEntityUpgrades> =
            CustomPacketPayload.Type(OtherUtil.modResource("update_entity_upgrades"))

        val STREAM_CODEC: StreamCodec<ByteBuf, UpdateEntityUpgrades> =
            StreamCodec.composite(
                ByteBufCodecs.INT, UpdateEntityUpgrades::entityId,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), UpdateEntityUpgrades::upgrades,
                ::UpdateEntityUpgrades
            )
    }

}