package dev.aaronhowser.mods.paracosm

import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.registry.ModRegistries
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Paracosm.ID)
class Paracosm(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "paracosm"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {
        LOGGER.info("Hello, Paracosm!")

        ModRegistries.register(MOD_BUS)

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
    }

}