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
			player: Entity,
			type: Holder<ResearchType>,
			amount: Int
		) {
			val current = player.getData(ModAttachmentTypes.RESEARCH_POINTS)
			val updated = current.addPoints(type, amount)
			player.setData(ModAttachmentTypes.RESEARCH_POINTS, updated)
		}

		fun removePoints(
			player: Entity,
			type: Holder<ResearchType>,
			amount: Int
		) {
			val current = player.getData(ModAttachmentTypes.RESEARCH_POINTS)
			val updated = current.removePoints(type, amount)
			player.setData(ModAttachmentTypes.RESEARCH_POINTS, updated)
		}
	}

}