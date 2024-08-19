package dev.aaronhowser.mods.paracosm.block.machine.imaginator

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ImaginatorBlock(
    properties: Properties = Properties.of()
        .sound(SoundType.WOOD)
        .strength(0.3f)
) : Block(properties) {

    // State

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(IS_CLOSED, true)
        )
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(IS_CLOSED, true)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_CLOSED)
    }

    override fun getInteractionShape(state: BlockState, level: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.block()
    }

    // Shape

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return if (state.getValue(IS_CLOSED)) SHAPE_CLOSED else SHAPE_OPEN
    }

    companion object {
        val IS_CLOSED: BooleanProperty = BooleanProperty.create("is_closed")

        private val BOTTOM = box(0.0, 0.0, 0.0, 16.0, 0.5, 16.0)
        private val NORTH = box(0.0, 0.0, 0.0, 16.0, 16.0, 0.5)
        private val EAST = box(15.5, 0.0, 0.0, 16.0, 16.0, 16.0)
        private val SOUTH = box(0.0, 0.0, 15.5, 16.0, 16.0, 16.0)
        private val WEST = box(0.0, 0.0, 0.0, 0.5, 16.0, 16.0)
        private val TOP = box(0.0, 15.5, 0.0, 16.0, 16.0, 16.0)

        private val SHAPE_OPEN = Shapes.or(BOTTOM, NORTH, EAST, SOUTH, WEST)
        private val SHAPE_CLOSED = Shapes.or(SHAPE_OPEN, TOP)
    }

    // Using

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val playerIsInBlock = player.blockPosition() == pos
        if (!playerIsInBlock) return InteractionResult.PASS

        val isClosed = state.getValue(IS_CLOSED)
        level.setBlock(pos, state.setValue(IS_CLOSED, !isClosed), 1 or 2)

        return InteractionResult.SUCCESS
    }

}