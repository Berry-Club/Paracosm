package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

object ResetScaleCommand {

	private const val TARGET_ARGUMENT = "target"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("resetScale")
			.executes { resetScale(it, null) }
			.then(
				Commands
					.argument(TARGET_ARGUMENT, EntityArgument.player())
					.requires { it.hasPermission(2) }
					.executes { resetScale(it, EntityArgument.getEntity(it, TARGET_ARGUMENT)) }
			)
	}

	private fun resetScale(
		context: CommandContext<CommandSourceStack>,
		entity: Entity?
	): Int {
		val commandSender = context.source
		val target = (entity ?: context.source.entity) as? Player ?: return 0

		target.shrinkRayEffect = 0.0

		target.sendSystemMessage(Component.literal("Your scale has been reset"))
		if (target != commandSender.entity) {
			commandSender.sendSystemMessage(Component.literal("${target.gameProfile.name} has had their scale reset"))
		}

		return 1
	}

}