package dev.aaronhowser.mods.paracosm.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult

class NightLightBlock : Block(
	Properties.of()
		.strength(0.5f)
		.mapColor(MapColor.COLOR_PINK)
		.sound(SoundType.GLASS)
		.lightLevel { if (it.getValue(ENABLED)) 8 else 0 }
		.noOcclusion()
		.pushReaction(PushReaction.DESTROY)
		.noCollission()
) {

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
		pBuilder.add(FACING, ENABLED)
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

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
		val ENABLED: BooleanProperty = BlockStateProperties.ENABLED
	}

}