package dev.aaronhowser.mods.paracosm.research

import com.mojang.serialization.Codec

data class ResearchType(
	val color: Int
) {

	companion object {
		val DIRECT_CODEC: Codec<ResearchType> = Codec.INT.xmap(::ResearchType, ResearchType::color)
	}

}