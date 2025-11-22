package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.CommandDispatcher
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ModCommands {

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

		dispatcher.register(
			Commands
				.literal(Paracosm.MOD_ID)
				.then(WhimsyCommand.register())
				.then(DelusionCommand.register())
				.then(ResetScaleCommand.register())
				.then(UpgradeCommand.register())
				.then(SetResearchCommand.register())
		)

	}

}