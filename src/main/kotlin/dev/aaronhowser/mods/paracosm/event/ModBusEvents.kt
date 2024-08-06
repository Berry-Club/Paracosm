package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.ICapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurio

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

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {

        event.registerItem(
            CuriosCapability.ITEM,
            ICapabilityProvider { stack, context ->
                object : ICurio {
                    override fun getStack(): ItemStack {
                        return stack
                    }

                    override fun curioTick(slotContext: SlotContext?) {
                        println("Curio tick")
                    }
                }
            },
            ModItems.SEEING_STONE.get()
        )

    }

}