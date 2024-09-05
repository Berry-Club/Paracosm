package dev.aaronhowser.mods.paracosm.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.component.StringListComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Paracosm.ID)

    val TOWEL_CAPE_WORKS: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("towel_cape_works") {
            it.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val UPGRADES: DeferredHolder<DataComponentType<*>, DataComponentType<StringListComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("upgrades") {
            it.persistent(StringListComponent.CODEC).networkSynchronized(StringListComponent.STREAM_CODEC)
        }

}