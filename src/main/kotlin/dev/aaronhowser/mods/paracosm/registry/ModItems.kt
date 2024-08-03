package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEM_REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(Paracosm.ID)

}