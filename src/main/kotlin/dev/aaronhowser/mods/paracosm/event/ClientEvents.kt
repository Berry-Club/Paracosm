package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.client.DuckHuntGunShaderHandler
import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(
	modid = "paracosm",
	value = [Dist.CLIENT]
)
object ClientEvents {

	@SubscribeEvent
	fun beforeClientTick(event: ClientTickEvent.Pre) {
		PogoStickVehicle.handleInput(event)
		DuckHuntGunShaderHandler.updateShader(event)
	}

}