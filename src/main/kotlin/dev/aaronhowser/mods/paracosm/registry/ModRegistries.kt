package dev.aaronhowser.mods.paracosm.registry

import net.neoforged.bus.api.IEventBus

object ModRegistries {

	fun register(modBus: IEventBus) {
		val registries = listOf(
			ModAttachmentTypes.ATTACHMENT_TYPES_REGISTRY,
			ModItems.ITEM_REGISTRY,
			ModDataComponents.DATA_COMPONENT_REGISTRY,
			ModBlocks.BLOCK_REGISTRY,
			ModBlockEntityTypes.BLOCK_ENTITY_REGISTRY,
			ModCreativeTabs.CREATIVE_TAB_REGISTRY,
			ModEntityTypes.ENTITY_TYPE_REGISTRY,
			ModSounds.SOUND_EVENT_REGISTRY,
			ModAttributes.ATTRIBUTE_REGISTRY,
			ModArmorMaterials.ARMOR_MATERIAL_REGISTRY,
			ModMobEffects.EFFECT_REGISTRY
		)

		registries.forEach { it.register(modBus) }
	}

}