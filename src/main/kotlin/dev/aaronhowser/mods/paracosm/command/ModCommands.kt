package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.CommandDispatcher
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack

object ModCommands : AaronCommandHelper {

	fun register(
		dispatcher: CommandDispatcher<CommandSourceStack>,
		buildContext: CommandBuildContext
	) {
		dispatcher.register(
			literal(Paracosm.MOD_ID) {
				then(WhimsyCommand.register())
				then(DelusionCommand.register())
				then(ResetScaleCommand.register())
				then(UpgradeCommand.register())
				then(SetResearchCommand.register())
			}
		)

	}

}