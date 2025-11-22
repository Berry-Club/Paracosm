package dev.aaronhowser.mods.paracosm.block.block_entity

import dev.aaronhowser.mods.paracosm.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class CityRugBlockEntity(
	pPos: BlockPos,
	pBlockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CITY_RUG.get(), pPos, pBlockState) {

	var origin: BlockPos? = null
	var childBlocks: List<BlockPos> = emptyList()

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val origin = origin
		if (origin != null) {
			tag.putLong(ORIGIN_NBT, origin.asLong())
		}

		if (childBlocks.isNotEmpty()) {
			val list = tag.getList(CHILD_BLOCKS_NBT, ListTag.TAG_LONG.toInt())

			for (childPos in childBlocks) {
				list.add(LongTag.valueOf(childPos.asLong()))
			}

			tag.put(CHILD_BLOCKS_NBT, list)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(ORIGIN_NBT)) {
			val originLong = tag.getLong(ORIGIN_NBT)
			origin = BlockPos.of(originLong)
		}

		if (tag.contains(CHILD_BLOCKS_NBT)) {
			val list = tag.getList(CHILD_BLOCKS_NBT, ListTag.TAG_LONG.toInt())

			for (i in 0 until list.size) {
				val longTag = list[i] as? LongTag ?: continue
				childBlocks += BlockPos.of(longTag.asLong)
			}
		}
	}

	companion object {
		const val ORIGIN_NBT = "Origin"
		const val CHILD_BLOCKS_NBT = "ChildBlocks"
	}

}