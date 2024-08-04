package dev.aaronhowser.mods.paracosm.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class CottonBlock(
    properties: Properties = Properties.of()
        .mapColor(MapColor.PLANT)
        .noCollission()
        .randomTicks()
        .instabreak()
        .sound(SoundType.CROP)
        .pushReaction(PushReaction.DESTROY)
) : CropBlock(properties) {

    companion object {
        val CODEC: MapCodec<CottonBlock> = simpleCodec(::CottonBlock)

        val AGE: IntegerProperty = BlockStateProperties.AGE_3
        val SHAPE_BY_AGE = arrayOf(
            box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AGE)
    }

    override fun getAgeProperty(): IntegerProperty {
        return AGE
    }

    override fun getMaxAge(): Int {
        return 3
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE_BY_AGE[getAge(state)]
    }

    override fun codec(): MapCodec<out CropBlock> {
        return CODEC
    }

    override fun getBaseSeedId(): ItemLike {
        return ModItems.COTTON
    }

}