package dev.aaronhowser.mods.paracosm.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
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

        placeAll(pos, level, state)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        val originPos = getOrigin(pos, level, state)
        val originState = level.getBlockState(originPos)

        breakAll(originPos, level, originState)

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    companion object {

        fun canPlaceAll(pos: BlockPos, level: Level, facing: Direction): Boolean {
            val clockwise = facing.clockWise

            for (i in 1..3) {
                val otherPos = pos.relative(clockwise, i)
                val otherState = level.getBlockState(otherPos)

                if (!otherState.canBeReplaced()) return false
            }

            for (i in 4..7) {
                val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
                val otherState = level.getBlockState(otherPos)

                if (!otherState.canBeReplaced()) return false
            }

            return true
        }

        fun placeAll(pos: BlockPos, level: Level, blockState: BlockState): Boolean {
            val direction = blockState.getValue(FACING)

            if (!canPlaceAll(pos, level, direction)) return false

            val clockwise = direction.clockWise    // You place the top left, and it places the rest to the right

            for (i in 1..3) {
                val otherPos = pos.relative(clockwise, i)
                val otherState = level.getBlockState(otherPos)

                if (otherState.isEmpty) {
                    level.setBlockAndUpdate(otherPos, blockState.setValue(SEGMENT, i))
                }
            }

            for (i in 4..7) {
                val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
                val otherState = level.getBlockState(otherPos)

                if (otherState.isEmpty) {
                    level.setBlockAndUpdate(otherPos, blockState.setValue(SEGMENT, i))
                }
            }

            return true
        }

        fun getOrigin(pos: BlockPos, level: Level, blockState: BlockState): BlockPos {
            val direction = blockState.getValue(FACING)
            val segment = blockState.getValue(SEGMENT)

            if (segment == 0) return pos

            val originX = pos.x - direction.clockWise.stepX * (segment - 1)
            val originZ = pos.z - direction.clockWise.stepZ * (segment - 1)

            return BlockPos(originX, pos.y, originZ)
        }

        fun breakAll(pos: BlockPos, level: Level, blockState: BlockState): Boolean {
            if (blockState.block != ModBlocks.CITY_RUG.get()) return false

            val direction = blockState.getValue(FACING)
            val clockwise = direction.clockWise

            for (i in 1..3) {
                val otherPos = pos.relative(clockwise, i)
                val otherState = level.getBlockState(otherPos)

                if (otherState.block == ModBlocks.CITY_RUG.get()) {
                    level.setBlockAndUpdate(otherPos, Blocks.AIR.defaultBlockState())
                }
            }

            for (i in 4..7) {
                val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
                val otherState = level.getBlockState(otherPos)

                if (otherState.block == ModBlocks.CITY_RUG.get()) {
                    level.setBlockAndUpdate(otherPos, Blocks.AIR.defaultBlockState())
                }
            }

            return true
        }

        val SEGMENT: IntegerProperty = IntegerProperty.create("segment", 0, 7)
        val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
        val CODEC: MapCodec<CityRugBlock> = simpleCodec(::CityRugBlock)
    }

    override fun codec(): MapCodec<CityRugBlock> {
        return CODEC
    }
}