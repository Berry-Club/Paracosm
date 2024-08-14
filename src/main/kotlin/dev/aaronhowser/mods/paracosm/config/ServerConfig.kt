package dev.aaronhowser.mods.paracosm.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

        val CONFIG: ServerConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var TOY_FLOP_RANGE: ModConfigSpec.DoubleValue
    }

    init {
        serverConfigs()
        builder.build()
    }

    private fun serverConfigs() {
        TOY_FLOP_RANGE = builder
            .comment("The range that a toy checks for players to hide from.")
            .defineInRange("Toy Flop Range", 80.0, 0.0, Double.MAX_VALUE)
    }

}