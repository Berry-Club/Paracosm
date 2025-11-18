package dev.aaronhowser.mods.paracosm.block

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

class CottonBlock : CropBlock(
	Properties.of()
		.mapColor(MapColor.PLANT)
		.noCollission()
		.randomTicks()
		.instabreak()
		.sound(SoundType.CROP)
		.pushReaction(PushReaction.DESTROY)
) {

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(AGE_3)
	}

	override fun getAgeProperty(): IntegerProperty = AGE_3
	override fun getMaxAge(): Int = 3
	override fun getBaseSeedId(): ItemLike = ModItems.COTTON

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		val age = getAge(state)
		return SHAPE_BY_AGE[age]
	}

	companion object {
		val AGE_3: IntegerProperty = BlockStateProperties.AGE_3

		val SHAPE_BY_AGE: List<VoxelShape> =
			listOf(
				box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
				box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
				box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
				box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
			)
	}


}