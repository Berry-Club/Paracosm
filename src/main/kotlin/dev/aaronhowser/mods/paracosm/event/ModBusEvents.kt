package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(
    modid = Paracosm.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        ModPacketHandler.registerPayloads(event)
    }

    @SubscribeEvent
    fun entityAttributeEvent(event: EntityAttributeCreationEvent) {
        event.put(ModEntityTypes.TEDDY_BEAR.get(), TeddyBearEntity.setAttributes())
    }

}