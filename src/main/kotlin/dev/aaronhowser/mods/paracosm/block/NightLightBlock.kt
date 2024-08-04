package dev.aaronhowser.mods.paracosm.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult

class NightLightBlock(
    private val properties: Properties = Properties.of()
        .strength(0.5f)
        .mapColor(MapColor.COLOR_PINK)
        .sound(SoundType.GLASS)
        .lightLevel { if (it.getValue(FACING) == Direction.NORTH) 15 else 3 }
        .noOcclusion()
        .pushReaction(PushReaction.DESTROY)
        .noCollission()
) : HorizontalDirectionalBlock(properties) {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ENABLED, true)
        )
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
            .setValue(ENABLED, true)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING, ENABLED)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    companion object {
        val ENABLED: BooleanProperty = BlockStateProperties.ENABLED

        val CODEC: MapCodec<NightLightBlock> = simpleCodec(::NightLightBlock)
    }

    override fun codec(): MapCodec<NightLightBlock> {
        return CODEC
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        level.setBlock(
            pos,
            state.setValue(ENABLED, !state.getValue(ENABLED)),
            3
        )

        return InteractionResult.SUCCESS
    }

}