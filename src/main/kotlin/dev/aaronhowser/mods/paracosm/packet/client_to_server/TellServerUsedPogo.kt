package dev.aaronhowser.mods.paracosm.packet.client_to_server

import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacket
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.TellClientsUsedPogo
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.network.handling.IPayloadContext

class TellServerUsedPogo private constructor() : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player() as? ServerPlayer ?: return@enqueueWork
            val heldPogo = PogoStickItem.getHeldPogoStick(player) ?: return@enqueueWork

            player.playSound(SoundEvents.PISTON_EXTEND)

            val packet = TellClientsUsedPogo(player)
            ModPacketHandler.messageNearbyPlayers(
                packet,
                player.level() as ServerLevel,
                player.eyePosition,
                64.0
            )

            var damageItemAmount = 0

            //TODO: Configurable, upgradeable?
            val damage = minOf(player.fallDistance, 6f)

            if (player.fallDistance >= 2f) {

                val box = AABB(
                    player.position().x - 0.3,
                    player.position().y - 0.0,
                    player.position().z - 0.3,
                    player.position().x + 0.3,
                    player.position().y + 0.6,
                    player.position().z + 0.3
                )

                for (entity in player.level().getEntities(player, box)) {
                    if (entity == player) continue

                    entity.hurt(player.level().damageSources().flyIntoWall(), damage)
                    damageItemAmount++
                }
            }

            val shouldFallDamageItem =
                !player.isCreative
                        && player.fallDistance > 1f
                        && !player.level()
                    .getBlockState(player.blockPosition().below())
                    .`is`(ModBlockTagsProvider.POGO_BOOST)

            if (shouldFallDamageItem) damageItemAmount++

            if (damageItemAmount > 0) {
                val equipmentSlot = player.getEquipmentSlotForItem(heldPogo)
                heldPogo.hurtAndBreak(damageItemAmount, player, equipmentSlot)
            }

            player.fallDistance = 0f
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