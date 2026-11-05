package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.paracosm.attachment.PlayerResearchPoints
import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

object SetResearchCommand : AaronCommandHelper {

	private const val RESEARCH_TYPE = "research-type"
	private const val AMOUNT = "amount"
	private const val PLAYER = "player"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("research") {
			requires { it.hasPermission(2) }

			thenLiteral("get-points") {
				thenArgument(RESEARCH_TYPE, ResourceLocationArgument.id()) {
					suggests(ModResearchTypes.COMMAND_SUGGESTION)
					executes {
						val source = it.source
						val player = source.playerOrException
						val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
						val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
						requireNotNull(researchType)

						getResearchPoints(source, player, researchType)
					}

					thenArgument(PLAYER, EntityArgument.player()) {
						executes {
							val source = it.source
							val player = EntityArgument.getPlayer(it, PLAYER)
							val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
							val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
							requireNotNull(researchType)

							getResearchPoints(source, player, researchType)
						}
					}
				}
			}

			thenLiteral("set-points") {
				thenArgument(RESEARCH_TYPE, ResourceLocationArgument.id()) {
					suggests(ModResearchTypes.COMMAND_SUGGESTION)

					thenArgument(AMOUNT, IntegerArgumentType.integer(0)) {
						executes {
							val source = it.source
							val player = source.playerOrException
							val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
							val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
							requireNotNull(researchType)
							val amount = IntegerArgumentType.getInteger(it, AMOUNT)

							setResearchPoints(source, player, researchType, amount)
						}

						thenArgument(PLAYER, EntityArgument.player()) {
							executes {
								val source = it.source
								val player = EntityArgument.getPlayer(it, PLAYER)
								val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
								val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
								requireNotNull(researchType)
								val amount = IntegerArgumentType.getInteger(it, AMOUNT)

								setResearchPoints(source, player, researchType, amount)
							}
						}
					}
				}
			}

			thenLiteral("add-points") {
				thenArgument(RESEARCH_TYPE, ResourceLocationArgument.id()) {
					suggests(ModResearchTypes.COMMAND_SUGGESTION)

					thenArgument(AMOUNT, IntegerArgumentType.integer(1)) {
						executes {
							val source = it.source
							val player = source.playerOrException
							val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
							val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
							requireNotNull(researchType)
							val amount = IntegerArgumentType.getInteger(it, AMOUNT)

							addResearchPoints(source, player, researchType, amount)
						}

						thenArgument(PLAYER, EntityArgument.player()) {
							executes {
								val source = it.source
								val player = EntityArgument.getPlayer(it, PLAYER)
								val resourceLocation = ResourceLocationArgument.getId(it, RESEARCH_TYPE)
								val researchType = ModResearchTypes.fromResourceLocation(source.registryAccess(), resourceLocation)
								requireNotNull(researchType)
								val amount = IntegerArgumentType.getInteger(it, AMOUNT)

								addResearchPoints(source, player, researchType, amount)
							}
						}
					}
				}
			}
		}
	}

	private fun addResearchPoints(
		source: CommandSourceStack,
		player: Player,
		researchType: Holder<ResearchType>,
		amount: Int
	): Int {
		PlayerResearchPoints.addPoints(player, researchType, amount)

		source.sendSuccess(
			{ Component.literal("Added: $amount") },
			false
		)

		return 1
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