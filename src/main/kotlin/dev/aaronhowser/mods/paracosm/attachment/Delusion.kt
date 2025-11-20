package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateWhimsyValue
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

data class Delusion(
	val amount: Float
) {

	companion object {

		val CODEC: Codec<Delusion> =
			Codec.FLOAT.xmap(::Delusion, Delusion::amount)

		var LivingEntity.delusion: Float
			get() = this.getData(ModAttachmentTypes.DELUSION).amount
			set(value) {
				this.setData(ModAttachmentTypes.DELUSION, Delusion(value))

				val level = this.level()
				if (level is ServerLevel) {
					ModPacketHandler.messageAllPlayers(
						UpdateWhimsyValue(
							this.id,
							value,
							false
						)
					)
				}

				val nameString = this.name.string
				if (level is ServerLevel) {
					Paracosm.LOGGER.debug("Delusion for $nameString set to $value")
				} else {
					Paracosm.LOGGER.debug("Delusion for $nameString set to $value on client")
				}

			}

	}

}