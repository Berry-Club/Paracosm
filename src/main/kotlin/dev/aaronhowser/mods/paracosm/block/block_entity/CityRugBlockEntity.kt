package dev.aaronhowser.mods.paracosm.block.block_entity

import dev.aaronhowser.mods.paracosm.registry.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class CityRugBlockEntity(
	pPos: BlockPos,
	pBlockState: BlockState
) : BlockEntity(ModBlockEntities.CITY_RUG.get(), pPos, pBlockState) {

	var origin: BlockPos? = null
	var childBlocks: List<BlockPos> = emptyList()

	companion object {
		const val ORIGIN_NBT = "Origin"
		const val CHILD_BLOCKS_NBT = "ChildBlocks"
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val origin = origin
		if (origin != null) {
			tag.putIntArray(ORIGIN_NBT, intArrayOf(origin.x, origin.y, origin.z))
		}

		if (childBlocks.isNotEmpty()) {
			val list = tag.getList(CHILD_BLOCKS_NBT, ListTag.TAG_INT_ARRAY.toInt())

			for (childPos in childBlocks) {
				val array = intArrayOf(childPos.x, childPos.y, childPos.z)
				val intArrayTag = IntArrayTag(array)
				list.add(intArrayTag)
			}

			tag.put(CHILD_BLOCKS_NBT, list)
		}

	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(ORIGIN_NBT)) {
			val originArray = tag.getIntArray(ORIGIN_NBT)
			origin = BlockPos(originArray[0], originArray[1], originArray[2])
		}

		if (tag.contains(CHILD_BLOCKS_NBT)) {
			val list = tag.getList(CHILD_BLOCKS_NBT, ListTag.TAG_INT_ARRAY.toInt())

			for (i in 0 until list.size) {
				val array = list[i] as IntArrayTag

				val x = array[0].asInt
				val y = array[1].asInt
				val z = array[2].asInt

				val childPos = BlockPos(x, y, z)
				childBlocks += childPos
			}
		}

	}

}