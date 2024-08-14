package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.paracosm.attachment.Delusion.Companion.delusion
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

object GetDelusionCommand {

    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("getDelusion")
            .requires { it.hasPermission(2) }
            .executes { setDelusion(it, null) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.player())
                    .executes { setDelusion(it, EntityArgument.getEntity(it, TARGET_ARGUMENT)) }
            )
    }

    private fun setDelusion(
        context: CommandContext<CommandSourceStack>,
        entity: Entity?
    ): Int {
        val commandSender = context.source
        val target = (entity ?: context.source.entity) as? Player ?: return 0

        val targetDelusion = target.delusion

        commandSender.sendSystemMessage(Component.literal("${target.gameProfile.name} has $targetDelusion Delusion"))

        return 1
    }

}