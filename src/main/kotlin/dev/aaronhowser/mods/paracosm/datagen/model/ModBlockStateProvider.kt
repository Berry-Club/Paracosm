package dev.aaronhowser.mods.paracosm.datagen.model

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.CityRugBlock
import dev.aaronhowser.mods.paracosm.block.CottonBlock
import dev.aaronhowser.mods.paracosm.block.NightLightBlock
import dev.aaronhowser.mods.paracosm.block.machine.imaginator.ImaginatorBlock
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
        walrus()
        imaginator()
        cityRug()
    }

    private fun cityRug() {
        val block = ModBlocks.CITY_RUG.get()

        getVariantBuilder(block)
            .forAllStates {
                val segment = it.getValue(CityRugBlock.SEGMENT)

                val texture = if (segment < 4) {
                    modLoc("block/city_rug_left")
                } else {
                    modLoc("block/city_rug_right")
                }

                val fromX = when (segment) {
                    0, 1, 2, 3 -> 0
                    4, 5, 6, 7 -> 16
                    else -> throw IllegalStateException("Invalid segment")
                }

                val fromZ = when (segment) {
                    0, 2, 4, 6 -> 0
                    1, 3, 5, 7 -> 16
                    else -> throw IllegalStateException("Invalid segment")
                }

                val toX = when (segment) {
                    0, 1, 2, 3 -> 16
                    4, 5, 6, 7 -> 0
                    else -> throw IllegalStateException("Invalid segment")
                }

                val toZ = when (segment) {
                    0, 2, 4, 6 -> 16
                    1, 3, 5, 7 -> 0
                    else -> throw IllegalStateException("Invalid segment")
                }

                val model = models()
                    .getBuilder("city_rug$segment")

                    .element()
                    .from(0f, 0f, 0f)
                    .to(16f, 1f, 16f)
                    .textureAll(texture.toString())

                    .face(Direction.UP)
                    .uvs(
                        fromX.toFloat(),
                        fromZ.toFloat(),
                        toX.toFloat(),
                        toZ.toFloat()
                    )
                    .end()

                    .end()

                ConfiguredModel
                    .builder()
                    .modelFile(model)
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/city_rug0"),
                existingFileHelper
            )
        )

    }

    private fun imaginator() {
        val block = ModBlocks.IMAGINATOR.get()

        getVariantBuilder(block)
            .forAllStates {
                val isClosed = it.getValue(ImaginatorBlock.IS_CLOSED)

                val modelLoc = if (isClosed) {
                    "block/imaginator_closed"
                } else {
                    "block/imaginator_open"
                }

                //TODO: Replace with actual texture
                val texture = mcLoc("block/oak_planks")

                val builder = models()
                    .getBuilder("${Paracosm.ID}:$modelLoc")
                    .texture("texture", texture)
                    .texture("particle", texture)

                val bottom = builder.element()
                    .from(0f, 0f, 0f)
                    .to(16f, 1f, 16f)
                    .textureAll("#texture")
                    .end()

                val north = builder.element()
                    .from(0f, 1f, 0f)
                    .to(16f, 16f, 1f)
                    .textureAll("#texture")
                    .end()
                val south = builder.element()
                    .from(0f, 1f, 15f)
                    .to(16f, 16f, 16f)
                    .textureAll("#texture")
                    .end()
                val east = builder.element()
                    .from(15f, 1f, 0f)
                    .to(16f, 16f, 16f)
                    .textureAll("#texture")
                    .end()
                val west = builder.element()
                    .from(0f, 1f, 0f)
                    .to(1f, 16f, 16f)
                    .textureAll("#texture")
                    .end()

                if (isClosed) {
                    val top = builder.element()
                        .from(0f, 15f, 0f)
                        .to(16f, 16f, 16f)
                        .textureAll("#texture")
                        .end()
                }

                ConfiguredModel
                    .builder()
                    .modelFile(
                        builder
                    )
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/imaginator_open"),
                existingFileHelper
            )
        )

    }

    private fun walrus() {
        val block = ModBlocks.WALRUS.get()

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
                    .modelFile(
                        models()
                            .getExistingFile(modLoc("block/walrus"))
                    )
                    .rotationY(yRotation)
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/walrus"),
                existingFileHelper
            )
        )

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