package dev.aaronhowser.mods.paracosm.command

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object SetWhimsyCommand {

    private const val AMOUNT_ARGUMENT = "amount"
    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("setWhimsy")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(AMOUNT_ARGUMENT, FloatArgumentType.floatArg(0f))
                    .executes { setWhimsy(it, null) }
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entity())
                            .executes { setWhimsy(it, EntityArgument.getEntity(it, TARGET_ARGUMENT)) }
                    )
            )
    }

    private fun setWhimsy(
        context: CommandContext<CommandSourceStack>,
        entity: Entity?
    ): Int {
        val target = (entity ?: context.source.entity) as? LivingEntity ?: return 0

        target.whimsy = FloatArgumentType.getFloat(context, AMOUNT_ARGUMENT)

        return 1
    }

}