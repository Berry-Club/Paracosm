package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.client.TeddyBearRenderer
import dev.aaronhowser.mods.paracosm.item.ToyGunItem
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ModelEvent

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

    @SubscribeEvent
    fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

        ItemProperties.register(
            ModItems.TOY_GUN.get(),
            OtherUtil.modResource("whimsical"),
            ItemPropertyFunction { stack, _, _, _ ->
                val item = stack.item as? ToyGunItem ?: return@ItemPropertyFunction 0f

                if (item.requiredWhimsy >= ClientUtil.whimsy) 1f else 0f
            }
        )

    }

}