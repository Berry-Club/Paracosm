package dev.aaronhowser.mods.paracosm.handler

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateRawWhimsyPacket
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.world.entity.LivingEntity

object DelusionHandler {

	var LivingEntity.rawDelusion: Double
		get() = this.getData(ModAttachmentTypes.DELUSION)
		set(value) {
			this.setData(ModAttachmentTypes.DELUSION, value)

			val level = this.level()
			if (level.isServerSide) {
				val packet = UpdateRawWhimsyPacket(this.id, value, isWhimsy = true)
				ModPacketHandler.messageAllPlayers(packet)
			}

			Paracosm.LOGGER.debug("Raw whimsy for ${this.name.string} set to $value")
		}

	fun LivingEntity.getDelusion(): Double {
		val raw = this.rawDelusion

		val temporaryWhimsy = this.getData(ModAttachmentTypes.TEMPORARY_WHIMSY)
		val mod = temporaryWhimsy.totalAmount()

		return raw + mod
	}

}