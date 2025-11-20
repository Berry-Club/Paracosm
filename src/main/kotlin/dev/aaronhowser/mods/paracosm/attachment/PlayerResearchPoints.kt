package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.core.Holder

data class PlayerResearchPoints(
	val points: Map<Holder<ResearchType>, Int>
) {

	companion object {
		val CODEC: Codec<PlayerResearchPoints> =
			Codec
				.unboundedMap(ResearchType.HOLDER_CODEC, Codec.INT)
				.xmap(::PlayerResearchPoints, PlayerResearchPoints::points)
	}

}