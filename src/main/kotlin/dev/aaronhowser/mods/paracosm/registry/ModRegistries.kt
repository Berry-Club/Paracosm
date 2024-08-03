package dev.aaronhowser.mods.paracosm.registry

import net.neoforged.bus.api.IEventBus

object ModRegistries {

    private val registries = listOf(
        ModItems.ITEM_REGISTRY
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}