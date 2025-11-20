package dev.aaronhowser.mods.paracosm.packet

import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import dev.aaronhowser.mods.paracosm.packet.client_to_server.UpdatePogoControls
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateShrinkRayScale
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketHandler : AaronPacketRegistrar {

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1")

		toClient(
			registrar,
			UpdateEntityUpgrades.TYPE,
			UpdateEntityUpgrades.STREAM_CODEC,
		)

		toClient(
			registrar,
			UpdateShrinkRayScale.TYPE,
			UpdateShrinkRayScale.STREAM_CODEC,
		)

		toServer(
			registrar,
			UpdatePogoControls.TYPE,
			UpdatePogoControls.STREAM_CODEC,
		)

	}

}