package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.CottonBlock
import dev.aaronhowser.mods.paracosm.block.NightLightBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

    val BLOCK_REGISTRY: DeferredRegister.Blocks =
        DeferredRegister.createBlocks(Paracosm.ID)

    val COTTON = registerBlock("cotton", makeBlockItem = false) { CottonBlock() }
    val NIGHT_LIGHT = registerBlock("night_light") { NightLightBlock() }

    private fun <T : Block> registerBlock(
        name: String,
        makeBlockItem: Boolean = true,
        supplier: () -> T = { Block(BlockBehaviour.Properties.of()) as T }
    ): DeferredBlock<T> {
        val block = BLOCK_REGISTRY.register(name, supplier)

        if (makeBlockItem) ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)

        return block
    }

}