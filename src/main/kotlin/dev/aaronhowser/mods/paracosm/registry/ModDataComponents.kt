package dev.aaronhowser.mods.paracosm.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.AaronExtraCodecs
import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents : AaronDataComponentRegistry() {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Paracosm.ID)

	override fun getDataComponentRegistry(): DeferredRegister.DataComponents {
		return DATA_COMPONENT_REGISTRY
	}

	val TOWEL_CAPE_WORKS: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("towel_cape_works")

	val ITEM_UPGRADES: DeferredHolder<DataComponentType<*>, DataComponentType<List<String>>> =
		register(
			"upgrades",
			Codec.STRING.listOf(),
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())
		)

	val AGGRO_IMMUNE_FROM: DeferredHolder<DataComponentType<*>, DataComponentType<List<TagKey<EntityType<*>>>>> =
		register(
			"aggro_immune_from",
			TagKey.codec(Registries.ENTITY_TYPE).listOf(),
			AaronExtraCodecs.tagKeyStreamCodec(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list())
		)

	val SCARES_ENTITY_TYPES: DeferredHolder<DataComponentType<*>, DataComponentType<List<TagKey<EntityType<*>>>>> =
		register(
			"scares_entity_types",
			TagKey.codec(Registries.ENTITY_TYPE).listOf(),
			AaronExtraCodecs.tagKeyStreamCodec(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list())
		)

}