package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateWhimsyValue
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

data class Whimsy(
    val amount: Float
) {

    constructor() : this(0f)

    companion object {

        val CODEC: Codec<Whimsy> =
            Codec.FLOAT.xmap(::Whimsy, Whimsy::amount)

        var LivingEntity.whimsy: Float
            get() = this.getData(ModAttachmentTypes.WHIMSY).amount
            set(value) {
                this.setData(ModAttachmentTypes.WHIMSY, Whimsy(value))

                val level = this.level()
                if (level is ServerLevel) {
                    ModPacketHandler.messageNearbyPlayers(
                        UpdateWhimsyValue(this.id, value, true),
                        level,
                        this.position(),
                        128.0
                    )
                }
            }

    }

}