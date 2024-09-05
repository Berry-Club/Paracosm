package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.world.entity.player.Player

object UpgradeCommand {

    private const val UPGRADE_ARGUMENT = "upgrade"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("upgrade")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .literal("add")
                    .then(
                        Commands
                            .argument(UPGRADE_ARGUMENT, StringArgumentType.string())
                            .executes(::addUpgrade)
                    )
            )
            .then(
                Commands
                    .literal("remove")
                    .then(
                        Commands
                            .argument(UPGRADE_ARGUMENT, StringArgumentType.string())
                            .executes(::removeUpgrade)
                    )
            )
    }

    private fun addUpgrade(commandContext: CommandContext<CommandSourceStack>): Int {
        val player = commandContext.source.entity as? Player ?: return 0
        val upgrade = StringArgumentType.getString(commandContext, UPGRADE_ARGUMENT)

        val heldItem = player.mainHandItem
        Upgradeable.addUpgrade(heldItem, upgrade)

        return 1
    }

    private fun removeUpgrade(commandContext: CommandContext<CommandSourceStack>): Int {
        val player = commandContext.source.entity as? Player ?: return 0
        val upgrade = StringArgumentType.getString(commandContext, UPGRADE_ARGUMENT)

        val heldItem = player.mainHandItem
        Upgradeable.removeUpgrade(heldItem, upgrade)

        return 1
    }

}