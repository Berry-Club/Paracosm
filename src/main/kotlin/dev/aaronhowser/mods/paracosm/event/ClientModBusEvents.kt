package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.client.TeddyBearRenderer
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.client.renderer.entity.EntityRenderers
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@EventBusSubscriber(
    modid = Paracosm.ID,
    bus = EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object ClientModBusEvents {

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {

        EntityRenderers.register(
            ModEntityTypes.TEDDY_BEAR.get(),
            ::TeddyBearRenderer
        )

    }

}