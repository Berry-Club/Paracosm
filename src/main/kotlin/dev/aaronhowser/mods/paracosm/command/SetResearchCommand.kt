package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.paracosm.attachment.PlayerResearchPoints
import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

object SetResearchCommand {

	private const val RESEARCH_TYPE = "research-type"
	private const val AMOUNT = "amount"
	private const val PLAYER = "player"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands.literal("research")
			.requires { it.hasPermission(2) }
			.then(
				Commands.literal("get-points")
					.then(
						Commands.argument(RESEARCH_TYPE, ResourceLocationArgument.id())
							.suggests(ModResearchTypes.COMMAND_SUGGESTION)
							.executes {
								val source = it.source
								val player = source.playerOrException
								val rl = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
								val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), rl)
								requireNotNull(researchType)
								getResearchPoints(source, player, researchType)
							}
							.then(
								Commands.argument(PLAYER, EntityArgument.player())
									.executes {
										val source = it.source
										val player = EntityArgument.getPlayer(it, PLAYER)
										val rl = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
										val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), rl)
										requireNotNull(researchType)
										getResearchPoints(source, player, researchType)
									}
							)
					)
			)
			.then(
				Commands.literal("set-points")
					.then(
						Commands.argument(RESEARCH_TYPE, ResourceLocationArgument.id())
							.suggests(ModResearchTypes.COMMAND_SUGGESTION)
							.then(
								Commands.argument(AMOUNT, IntegerArgumentType.integer(0))
									.executes {
										val source = it.source
										val player = source.playerOrException
										val rl = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
										val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), rl)
										requireNotNull(researchType)
										val amount = IntegerArgumentType.getInteger(it, AMOUNT)
										setResearchPoints(source, player, researchType, amount)
									}
									.then(
										Commands.argument(PLAYER, EntityArgument.player())
											.executes {
												val source = it.source
												val player = EntityArgument.getPlayer(it, PLAYER)
												val rl = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
												val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), rl)
												requireNotNull(researchType)
												val amount = IntegerArgumentType.getInteger(it, AMOUNT)
												setResearchPoints(source, player, researchType, amount)
											}
									)
							)
					)
			)
	}

	private fun setResearchPoints(
		source: CommandSourceStack,
		player: Player,
		researchType: Holder<ResearchType>,
		amount: Int
	): Int {
		PlayerResearchPoints.setPoints(player, researchType, amount)

		source.sendSuccess(
			{ Component.literal("Set to: $amount") },
			false
		)

		return 1
	}

	private fun getResearchPoints(
		source: CommandSourceStack,
		player: Player,
		researchType: Holder<ResearchType>
	): Int {
		val amount = PlayerResearchPoints.getPoints(player, researchType)

		source.sendSuccess(
			{ Component.literal("Amount: $amount") },
			false
		)

		return 1
	}

}