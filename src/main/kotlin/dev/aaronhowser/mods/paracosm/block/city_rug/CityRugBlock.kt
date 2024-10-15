package dev.aaronhowser.mods.paracosm.block.city_rug

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class CityRugBlock(
    properties: Properties = Properties.of()
        .strength(0.1f)
        .sound(SoundType.WOOL)
        .ignitedByLava()
        .mapColor(MapColor.COLOR_LIGHT_GRAY)
) : HorizontalDirectionalBlock(properties), EntityBlock {

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

        val success = placeAll(pos, level, state)

        if (!success) {
            level.removeBlock(pos, false)
        }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        breakAll(pos, level)

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        if (level.isEmptyBlock(pos.below())) return false

        val segment = state.getValue(SEGMENT)
        if (segment != 0) return true

        val facing = state.getValue(FACING)
        return canPlaceAll(pos, level, facing)
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        return if (neighborState.block == Blocks.AIR && pos.below() == neighborPos) {
            Blocks.AIR.defaultBlockState()
        } else {
            super.updateShape(state, direction, neighborState, level, pos, neighborPos)
        }
    }

    companion object {

        fun breakAll(pos: BlockPos, level: Level) {
            val blockEntity = level.getBlockEntity(pos) as? CityRugBlockEntity ?: return
            val origin = blockEntity.origin ?: return
            val originEntity = level.getBlockEntity(origin) as? CityRugBlockEntity ?: return

            for (childPos in originEntity.childBlocks) {
                level.setBlockAndUpdate(childPos, Blocks.AIR.defaultBlockState())
            }

            level.setBlockAndUpdate(origin, Blocks.AIR.defaultBlockState())
        }

        fun canPlaceAll(pos: BlockPos, level: LevelReader, facing: Direction): Boolean {
            val clockwise = facing.clockWise

            fun isPosGood(otherPos: BlockPos): Boolean {
                if (level.isEmptyBlock(otherPos.below())) return false

                val otherState = level.getBlockState(otherPos)
                if (!otherState.canBeReplaced()) return false

                if (level is Level) {
                    val entityInBlock = level.getNearestEntity(
                        LivingEntity::class.java,
                        TargetingConditions
                            .forNonCombat()
                            .range(1.0)
                            .ignoreInvisibilityTesting()
                            .ignoreLineOfSight(),
                        null,
                        otherPos.x.toDouble() + 0.5,
                        otherPos.y.toDouble(),
                        otherPos.z.toDouble() + 0.5,
                        AABB.ofSize(otherPos.bottomCenter, 1.0, 1.0 / 16, 1.0)
                    )

                    if (entityInBlock != null) return false
                }

                return true
            }

            for (i in 1..3) {
                val otherPos = pos.relative(clockwise, i)
                if (!isPosGood(otherPos)) return false
            }

            for (i in 4..7) {
                val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
                if (!isPosGood(otherPos)) return false
            }

            return true
        }

        fun placeAll(pos: BlockPos, level: Level, blockState: BlockState): Boolean {
            val direction = blockState.getValue(FACING)
            if (!canPlaceAll(pos, level, direction)) return false

            val clockwise = direction.clockWise    // You place the top left, and it places the rest to the right

            val childBlocks = mutableListOf<BlockPos>()

            for (i in 1..3) {
                val otherPos = pos.relative(clockwise, i)
                val otherState = level.getBlockState(otherPos)

                if (otherState.isEmpty) {
                    level.setBlockAndUpdate(otherPos, blockState.setValue(SEGMENT, i))

                    val thatBe = level.getBlockEntity(otherPos)
                    if (thatBe is CityRugBlockEntity) {
                        thatBe.origin = pos
                        thatBe.setChanged()
                    }

                    childBlocks.add(otherPos)
                }
            }

            for (i in 4..7) {
                val otherPos = pos.relative(clockwise, i - 4).relative(clockwise.clockWise, 1)
                val otherState = level.getBlockState(otherPos)

                if (otherState.isEmpty) {
                    level.setBlockAndUpdate(otherPos, blockState.setValue(SEGMENT, i))

                    val thatBe = level.getBlockEntity(otherPos)
                    if (thatBe is CityRugBlockEntity) {
                        thatBe.origin = pos
                        thatBe.setChanged()
                    }

                    childBlocks.add(otherPos)
                }
            }

            val thisBe = level.getBlockEntity(pos)
            if (thisBe is CityRugBlockEntity) {
                thisBe.origin = pos
                thisBe.childBlocks = childBlocks

                thisBe.setChanged()
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

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CityRugBlockEntity(pPos, pState)
    }
}