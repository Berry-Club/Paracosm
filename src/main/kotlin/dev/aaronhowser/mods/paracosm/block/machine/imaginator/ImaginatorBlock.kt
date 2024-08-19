package dev.aaronhowser.mods.paracosm.block.machine.imaginator

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ImaginatorBlock(
    properties: Properties = Properties.of()
        .sound(SoundType.WOOD)
        .strength(0.3f)
) : Block(properties) {

    override fun getInteractionShape(state: BlockState, level: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.block()
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    companion object {

        private val BOTTOM = box(0.0, 0.0, 0.0, 16.0, 0.5, 16.0)
        private val NORTH = box(0.0, 0.0, 0.0, 16.0, 16.0, 0.5)
        private val EAST = box(15.5, 0.0, 0.0, 16.0, 16.0, 16.0)
        private val SOUTH = box(0.0, 0.0, 15.5, 16.0, 16.0, 16.0)
        private val WEST = box(0.0, 0.0, 0.0, 0.5, 16.0, 16.0)

        private val SHAPE = Shapes.or(BOTTOM, NORTH, EAST, SOUTH, WEST)
    }

}