package dev.aaronhowser.mods.paracosm.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
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

}