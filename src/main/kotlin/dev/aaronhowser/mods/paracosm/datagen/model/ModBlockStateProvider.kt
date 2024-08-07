package dev.aaronhowser.mods.paracosm.datagen.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.CottonBlock
import dev.aaronhowser.mods.paracosm.block.NightLightBlock
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
        cotton()
        whoopeeCushion()
    }

    private fun whoopeeCushion() {
        val block = ModBlocks.WHOOPEE_CUSHION.get()

        pressurePlateBlock(block, modLoc("block/whoopee_cushion"))

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/whoopee_cushion"),
                existingFileHelper
            )
        )
    }

    private fun nightLight() {
        val block = ModBlocks.NIGHT_LIGHT.get()

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

                val enabled = it.getValue(NightLightBlock.ENABLED)
                val modelLoc = if (enabled) {
                    modLoc("block/night_light_on")
                } else {
                    modLoc("block/night_light")
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
                modLoc("block/night_light"),
                existingFileHelper
            )
        )
    }

    private fun cotton() {
        val block = ModBlocks.COTTON.get()

        getVariantBuilder(block)
            .forAllStates {
                val age = it.getValue(CottonBlock.AGE)

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .crop(
                                "cotton_stage$age",
                                modLoc("block/cotton_stage$age"),
                            )
                            .renderType("minecraft:cutout")
                    )
                    .build()
            }

    }

}