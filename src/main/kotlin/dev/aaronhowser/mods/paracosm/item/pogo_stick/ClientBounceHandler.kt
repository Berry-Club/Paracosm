package dev.aaronhowser.mods.paracosm.item.pogo_stick

import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.client_to_server.TellServerUsedPogo
import net.minecraft.client.player.LocalPlayer
import net.neoforged.neoforge.event.entity.living.LivingFallEvent
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent

object ClientBounceHandler {

    private fun handlePlayerFall(player: LocalPlayer) {
        val isSneaking = player.isSuppressingBounce
        if (isSneaking) return

        bounceUp(player)
        applyVelocityAdjustments(player)
        handleBoost(player)
    }

    private fun bounceUp(player: LocalPlayer) {
        val isJumping = player.input.jumping
        player.setDeltaMovement(
            player.deltaMovement.x * 1.05,
            player.deltaMovement.y * if (isJumping) -2.0 else -1.1,
            player.deltaMovement.z * 1.05
        )
    }

    private fun applyVelocityAdjustments(player: LocalPlayer) {
        val isJumping = player.input.jumping
        val minVelocity = if (isJumping) 0.0 else 0.2
        val jumpValue = 1.0
        val jumpMult = 1.5

        if (player.deltaMovement.y < minVelocity) {
            player.setDeltaMovement(
                player.deltaMovement.x,
                0.0,
                player.deltaMovement.z
            )
        } else if (isJumping && player.deltaMovement.y > jumpValue) {
            player.setDeltaMovement(
                player.deltaMovement.x,
                minOf(player.deltaMovement.y * jumpMult, jumpValue),
                player.deltaMovement.z
            )
        }

        player.setOnGround(false)
    }

    private fun handleBoost(player: LocalPlayer) {
        val isJumping = player.input.jumping
        var motion = player.deltaMovement.y * if (isJumping) 0.5 else 1.0

        val posIsBoost =
            player.level().getBlockState(player.blockPosition()).`is`(ModBlockTagsProvider.POGO_BOOST)
        val belowIsBoost =
            player.level().getBlockState(player.blockPosition().below()).`is`(ModBlockTagsProvider.POGO_BOOST)

        val shouldBoost = posIsBoost || belowIsBoost

        if (shouldBoost) {
            motion *= 1.5
        }

        if (motion != 0.0) {
            CommonBounceHandler.addBouncer(player, motion)
            ModPacketHandler.messageServer(TellServerUsedPogo.INSTANCE)
        } else {
            CommonBounceHandler.removeBouncer(player)
        }
    }

    fun handleEvent(event: PlayerFlyableFallEvent) {
        val player = event.entity as? LocalPlayer ?: return

        if (!shouldPlayerBounce(player, event.distance)) return

        handlePlayerFall(player)

        player.fallDistance = 0f
    }

    fun handleEvent(event: LivingFallEvent) {
        val player = event.entity as? LocalPlayer ?: return

        if (!shouldPlayerBounce(player, event.distance)) return

        handlePlayerFall(player)

        event.isCanceled = true
        event.damageMultiplier = 0f
        player.fallDistance = 0f
    }

    private fun shouldPlayerBounce(player: LocalPlayer, distance: Float): Boolean {
        return distance >= 1 && PogoStickItem.getHeldPogoStick(player) != null
    }

}