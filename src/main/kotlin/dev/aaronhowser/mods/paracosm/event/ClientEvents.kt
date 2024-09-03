package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.pogo_stick.ClientBounceHandler
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingFallEvent
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent

@EventBusSubscriber(
    modid = Paracosm.ID,
    value = [Dist.CLIENT]
)
object ClientEvents {

    @SubscribeEvent
    fun onLivingFall(event: LivingFallEvent) {
        ClientBounceHandler.handleEvent(event)
    }

    @SubscribeEvent
    fun onFlyableFall(event: PlayerFlyableFallEvent) {
        ClientBounceHandler.handleEvent(event)
    }

}