package dev.aaronhowser.mods.paracosm.datagen.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    output: PackOutput,
    private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, Paracosm.ID, existingFileHelper) {

    override fun registerStatesAndModels() {
        nightLight()
    }

    private fun nightLight() {
        val block = ModBlocks.NIGHT_LIGHT.get()

        val modelLoc = modLoc("block/night_light")

        getVariantBuilder(block)
            .forAllStates {
                val facing = it.getValue(HorizontalDirectionalBlock.FACING)
                val yRotation = when (facing) {
                    Direction.NORTH -> 0
                    Direction.EAST -> 90
                    Direction.SOUTH -> 180
                    Direction.WEST -> 270
                    else -> throw IllegalStateException("Invalid facing direction")
                }

                ConfiguredModel
                    .builder()
                    .modelFile(models().getExistingFile(modelLoc))
                    .rotationY(yRotation)
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/conductor"),
                existingFileHelper
            )
        )
    }

}