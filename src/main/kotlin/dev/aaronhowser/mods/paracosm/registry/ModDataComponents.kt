package dev.aaronhowser.mods.paracosm.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Paracosm.ID)

    val TOWEL_CAPE_WORKS: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("towel_cape_works") {
            it.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val ITEM_UPGRADES: DeferredHolder<DataComponentType<*>, DataComponentType<List<String>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("upgrades") {
            it.persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()))
        }

}