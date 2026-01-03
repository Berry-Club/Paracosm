package dev.aaronhowser.mods.paracosm

import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.registry.ModRegistries
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Paracosm.MOD_ID)
class Paracosm(
	modContainer: ModContainer
) {

	companion object {
		const val MOD_ID = "paracosm"
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): ResourceLocation =
			ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
	}

	init {
		LOGGER.info("Hello, Paracosm!")

		ModRegistries.register(MOD_BUS)

		modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
	}

}