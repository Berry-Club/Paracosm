package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.city_rug.CityRugBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlockEntities {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Paracosm.ID)

	val CITY_RUG: DeferredHolder<BlockEntityType<*>, BlockEntityType<CityRugBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("city_rug", Supplier {
			BlockEntityType.Builder.of(
				{ pos, state -> CityRugBlockEntity(pos, state) },
				ModBlocks.CITY_RUG.get()
			).build(null)
		})

}