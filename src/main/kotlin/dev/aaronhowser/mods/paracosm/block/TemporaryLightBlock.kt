package dev.aaronhowser.mods.paracosm.block

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

class TemporaryLightBlock : AirBlock(
	Properties.ofFullCopy(Blocks.AIR)
		.lightLevel { 10 }
) {

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		super.onPlace(state, level, pos, oldState, movedByPiston)
		level.scheduleTick(pos, this, 3)
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		super.tick(state, level, pos, random)
		if (level.getBlockState(pos).`is`(this)) {
			level.removeBlock(pos, false)

			val entitiesInPos = level.getEntitiesOfClass(LivingEntity::class.java, AABB(pos))
			val isWearingShoes = entitiesInPos.any { it.getItemBySlot(EquipmentSlot.FEET).`is`(ModItems.LIGHT_UP_SHOES) }
			if (isWearingShoes) {
				level.setBlock(pos, this.defaultBlockState(), UPDATE_CLIENTS)
			}
		}
	}

}