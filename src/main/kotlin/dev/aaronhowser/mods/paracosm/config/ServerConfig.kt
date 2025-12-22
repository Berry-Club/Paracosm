package dev.aaronhowser.mods.paracosm.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var toyFlopRange: ModConfigSpec.DoubleValue
	lateinit var pogoGoombaRadius: ModConfigSpec.DoubleValue

	lateinit var hulaHoopCostPerPush: ModConfigSpec.DoubleValue
	lateinit var hulaHoopPushScale: ModConfigSpec.DoubleValue
	lateinit var hulaHoopMomentumPerRotation: ModConfigSpec.DoubleValue
	lateinit var hulaHoopBleedPerTick: ModConfigSpec.DoubleValue

	init {
		generalConfigs()
		hulaHoop()
		builder.build()
	}

	private fun generalConfigs() {
		toyFlopRange = builder
			.comment("The range that a toy checks for players to hide from.")
			.defineInRange("Toy Flop Range", 80.0, 0.0, Double.MAX_VALUE)

		pogoGoombaRadius = builder
			.comment("The radius that a pogo stick checks for entities to stomp.")
			.defineInRange("Pogo Goomba Radius", 1.5, 0.0, Double.MAX_VALUE)
	}

	private fun hulaHoop() {
		builder.push("Hula Hoop")

		hulaHoopCostPerPush = builder
			.comment("How much momentum to drain when pushing an entity.")
			.defineInRange("Hula Hoop Momentum Per Push", 1.0, 0.0, Double.MAX_VALUE)

		hulaHoopPushScale = builder
			.comment("Scale factor for how much velocity is applied to an entity when pushed by a hula hoop.")
			.defineInRange("Hula Hoop Push Scale", 1.0, 0.0, Double.MAX_VALUE)

		hulaHoopMomentumPerRotation = builder
			.comment("The amount of angular momentum added to a Hula Hoop per full circle rotated.")
			.defineInRange("Hula Hoop Momentum Per Rotation", 1.0, 0.0, Double.MAX_VALUE)

		hulaHoopBleedPerTick = builder
			.comment("The amount of angular momentum a hula hoop loses per tick.")
			.defineInRange("Hula Hoop Bleed Per Tick", 0.05, 0.0, Double.MAX_VALUE)

		builder.pop()
	}

	companion object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}