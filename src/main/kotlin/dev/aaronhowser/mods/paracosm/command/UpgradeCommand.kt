package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.aaronhowser.mods.paracosm.item.IUpgradeable
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
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

    private fun getSuggestions(): SuggestionProvider<CommandSourceStack> {
        val upgrades = mutableSetOf<String>()

        for (item in ModItems.ITEM_REGISTRY.registry.get()) {
            if (item is IUpgradeable) {
                upgrades.addAll(item.possibleUpgrades)
            }
        }

        return SuggestionProvider { _, suggestionsBuilder ->
            SharedSuggestionProvider.suggest(upgrades, suggestionsBuilder)
        }

    }

}