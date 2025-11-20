package dev.aaronhowser.mods.paracosm.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ResearchType(
	val color: Int
) {

	companion object {
		val DIRECT_CODEC: Codec<ResearchType> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("color")
						.forGetter(ResearchType::color)
				).apply(instance, ::ResearchType)
			}
	}

}