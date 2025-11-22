package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.block_entity.CityRugBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlockEntityTypes : AaronBlockEntityTypeRegistry() {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Paracosm.MOD_ID)

	override fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>> {
		return BLOCK_ENTITY_REGISTRY
	}

	val CITY_RUG: DeferredHolder<BlockEntityType<*>, BlockEntityType<CityRugBlockEntity>> =
		register(
			"city_rug",
			::CityRugBlockEntity,
			ModBlocks.CITY_RUG
		)

}