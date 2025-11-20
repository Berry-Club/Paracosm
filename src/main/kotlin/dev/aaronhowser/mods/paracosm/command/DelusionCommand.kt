package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object DelusionCommand {

	private const val TARGET = "target"
	private const val AMOUNT = "amount"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("delusion")
			.requires { it.hasPermission(2) }
			.then(
				Commands.literal("get")
					.executes {
						val source = it.source
						val target = source.playerOrException

						getDelusion(source, target)
					}
					.then(
						Commands.argument(TARGET, EntityArgument.player())
							.executes {
								val source = it.source
								val target = EntityArgument.getPlayer(it, TARGET)

								getDelusion(source, target)
							}
					)
			)
			.then(
				Commands.literal("set-base")
					.then(
						Commands.argument(AMOUNT, DoubleArgumentType.doubleArg(0.0, Double.MAX_VALUE))
							.executes {
								val source = it.source
								val target = source.playerOrException
								val amount = DoubleArgumentType.getDouble(it, AMOUNT)

								setBaseDelusion(source, target, amount)
							}
							.then(
								Commands.argument(TARGET, EntityArgument.player())
									.executes {
										val source = it.source
										val target = EntityArgument.getPlayer(it, TARGET)
										val amount = DoubleArgumentType.getDouble(it, AMOUNT)

										setBaseDelusion(source, target, amount)
									}
							)
					)
			)
	}

	private fun setBaseDelusion(
		source: CommandSourceStack,
		target: Entity,
		amount: Double
	): Int {
		if (target !is LivingEntity) {
			source.sendFailure(Component.literal("Target must be a living entity"))
			return 0
		}

		val attribute = target.getAttribute(ModAttributes.DELUSION)
		if (attribute == null) {
			source.sendFailure(Component.literal("Target does not have Delusion attribute"))
			return 0
		}

		attribute.baseValue = amount

		source.sendSuccess(
			{
				Component.literal("Set ${target.name.string}'s Delusion base to $amount")
			},
			false
		)

		return 1
	}

	private fun getDelusion(
		source: CommandSourceStack,
		target: Entity
	): Int {
		if (target !is LivingEntity) {
			source.sendFailure(Component.literal("Target must be a living entity"))
			return 0
		}

		val attribute = target.getAttribute(ModAttributes.DELUSION)
		if (attribute == null) {
			source.sendFailure(Component.literal("Target does not have Delusion attribute"))
			return 0
		}

		val base = attribute.baseValue
		val current = attribute.value

		source.sendSuccess(
			{
				Component.literal("${target.name.string}'s Delusion - Base: $base, Current: $current")
			},
			false
		)

		return 1
	}

}