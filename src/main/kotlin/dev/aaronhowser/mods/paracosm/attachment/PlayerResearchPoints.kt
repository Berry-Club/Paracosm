package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.core.Holder
import net.minecraft.world.entity.Entity

data class PlayerResearchPoints(
	val points: Map<Holder<ResearchType>, Int>
) {

	private fun addPoints(
		type: Holder<ResearchType>,
		amount: Int
	): PlayerResearchPoints {
		val currentTypePoints = points.getOrDefault(type, 0)
		val newTypePoints = currentTypePoints + amount

		val newPoints = points.toMutableMap()
		newPoints[type] = newTypePoints

		return PlayerResearchPoints(newPoints)
	}

	private fun removePoints(
		type: Holder<ResearchType>,
		amount: Int
	): PlayerResearchPoints {
		val currentTypePoints = points.getOrDefault(type, 0)
		val newTypePoints = (currentTypePoints - amount).coerceAtLeast(0)

		val newPoints = points.toMutableMap()
		newPoints[type] = newTypePoints

		return PlayerResearchPoints(newPoints)
	}

	companion object {
		val CODEC: Codec<PlayerResearchPoints> =
			Codec
				.unboundedMap(ResearchType.HOLDER_CODEC, Codec.INT)
				.xmap(::PlayerResearchPoints, PlayerResearchPoints::points)

		fun addPoints(
			entity: Entity,
			type: Holder<ResearchType>,
			amount: Int
		) {
			val current = entity.getData(ModAttachmentTypes.RESEARCH_POINTS)
			val updated = current.addPoints(type, amount)
			entity.setData(ModAttachmentTypes.RESEARCH_POINTS, updated)
		}

		fun removePoints(
			entity: Entity,
			type: Holder<ResearchType>,
			amount: Int
		): Boolean {
			if (!hasPoints(entity, type, amount)) {
				return false
			}

			val current = entity.getData(ModAttachmentTypes.RESEARCH_POINTS)
			val updated = current.removePoints(type, amount)
			entity.setData(ModAttachmentTypes.RESEARCH_POINTS, updated)

			return true
		}

		fun getPoints(
			entity: Entity,
			type: Holder<ResearchType>
		): Int {
			val current = entity.getData(ModAttachmentTypes.RESEARCH_POINTS)
			return current.points.getOrDefault(type, 0)
		}

		fun hasPoints(
			entity: Entity,
			type: Holder<ResearchType>,
			amount: Int
		): Boolean {
			val currentPoints = getPoints(entity, type)
			return currentPoints >= amount
		}
	}

}