package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import dev.aaronhowser.mods.paracosm.command.ModCommands
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent

@EventBusSubscriber(
    modid = Paracosm.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

    @SubscribeEvent
    fun afterUseItem(event: LivingEntityUseItemEvent.Finish) {
        val entity = event.entity

        val food = event.item.getFoodProperties(entity)
        if (food != null) {

            if (event.item.`is`(ModItemTagsProvider.SWEETS)) {
                entity.whimsy += 0.5f
            }

        }
    }

}