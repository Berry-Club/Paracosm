package dev.aaronhowser.mods.paracosm.registry

import net.neoforged.bus.api.IEventBus

object ModRegistries {

    private val registries = listOf(
        ModAttachmentTypes.ATTACHMENT_TYPES_REGISTRY,
        ModItems.ITEM_REGISTRY,
        ModDataComponents.DATA_COMPONENT_REGISTRY,
        ModBlocks.BLOCK_REGISTRY,
        ModCreativeTabs.CREATIVE_TAB_REGISTRY,
        ModEntityTypes.ENTITY_TYPE_REGISTRY,
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}