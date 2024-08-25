package dev.aaronhowser.mods.paracosm.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class CityRugBlock(
    properties: Properties = Properties.of()
        .strength(0.1f)
        .sound(SoundType.WOOL)
        .ignitedByLava()
        .mapColor(MapColor.COLOR_LIGHT_GRAY)
) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SEGMENT, 0)
        )
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, context.horizontalDirection)
            .setValue(SEGMENT, 0)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FACING, SEGMENT)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        super.onPlace(state, level, pos, oldState, movedByPiston)

        val segment = state.getValue(SEGMENT)
        if (segment != 0) return

        val facing = state.getValue(FACING)
        val clockwise = facing.clockWise    // You place the top left, and it places the rest to the right

        for (i in 1..3) {
            val otherPos = pos.relative(clockwise, i)
            val otherState = level.getBlockState(otherPos)

            if (otherState.isEmpty) {
                level.setBlockAndUpdate(otherPos, state.setValue(SEGMENT, i))
            }
        }

        for (i in 4..7) {
            val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
            val otherState = level.getBlockState(otherPos)

            if (otherState.isEmpty) {
                level.setBlockAndUpdate(otherPos, state.setValue(SEGMENT, i))
            }
        }

    }

    companion object {
        val SEGMENT: IntegerProperty = IntegerProperty.create("segment", 0, 7)
        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
        val CODEC: MapCodec<CityRugBlock> = simpleCodec(::CityRugBlock)
    }

    override fun codec(): MapCodec<CityRugBlock> {
        return CODEC
    }
}