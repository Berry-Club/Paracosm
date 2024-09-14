package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.aaronhowser.mods.paracosm.item.IUpgradeable
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component
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
                            .suggests(getSuggestions())
                            .executes(::addUpgrade)
                    )
            )
            .then(
                Commands
                    .literal("remove")
                    .then(
                        Commands
                            .argument(UPGRADE_ARGUMENT, StringArgumentType.string())
                            .suggests(getSuggestions())
                            .executes(::removeUpgrade)
                    )
            )
    }

    private fun addUpgrade(commandContext: CommandContext<CommandSourceStack>): Int {
        val player = commandContext.source.entity as? Player ?: return 0
        val upgrade = StringArgumentType.getString(commandContext, UPGRADE_ARGUMENT)

        val heldStack = player.mainHandItem

        val heldItem = heldStack.item
        if (heldItem !is IUpgradeable) {
            player.sendSystemMessage(Component.literal("This item cannot be upgraded"))
            return 0
        } else if (upgrade !in heldItem.possibleUpgrades) {
            player.sendSystemMessage(Component.literal("This item cannot be upgraded with $upgrade"))
            return 0
        }

        Upgradeable.addUpgrade(heldStack, upgrade)

        return 1
    }

    private fun removeUpgrade(commandContext: CommandContext<CommandSourceStack>): Int {
        val player = commandContext.source.entity as? Player ?: return 0
        val upgrade = StringArgumentType.getString(commandContext, UPGRADE_ARGUMENT)

        val heldItem = player.mainHandItem
        if (heldItem.item !is IUpgradeable) {
            player.sendSystemMessage(Component.literal("This item cannot be upgraded"))
            return 0
        }

        if (!Upgradeable.hasUpgrade(heldItem, upgrade)) {
            player.sendSystemMessage(Component.literal("This item does not have the upgrade $upgrade"))
            return 0
        }

        Upgradeable.removeUpgrade(heldItem, upgrade)

        return 1
    }

    private fun getSuggestions(): SuggestionProvider<CommandSourceStack> {
        return SuggestionProvider { context, suggestionsBuilder ->
            val player = context.source.entity as? Player ?: return@SuggestionProvider suggestionsBuilder.buildFuture()

            val heldItem = player.mainHandItem.item
            if (heldItem !is IUpgradeable) return@SuggestionProvider suggestionsBuilder.buildFuture()

            SharedSuggestionProvider.suggest(heldItem.possibleUpgrades, suggestionsBuilder)
        }
    }

}