package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GetDelusionCommand {

	private const val TARGET_ARGUMENT = "target"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("getDelusion")
			.requires { it.hasPermission(2) }
			.executes {
				val source = it.source
				val target = source.playerOrException

				getDelusion(source, target)
			}
			.then(
				Commands.argument(TARGET_ARGUMENT, EntityArgument.player())
					.executes {
						val source = it.source
						val target = EntityArgument.getPlayer(it, TARGET_ARGUMENT)

						getDelusion(source, target)
					}
			)
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