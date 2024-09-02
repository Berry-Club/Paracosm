package dev.aaronhowser.mods.paracosm.item.pogo_stick

import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.client_to_server.TellServerUsedPogo
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingFallEvent
import net.neoforged.neoforge.event.entity.player.PlayerFlyableFallEvent

object BounceHandler {

    private val bouncers: MutableMap<Player, Bouncing> = mutableMapOf()

    fun addBouncer(player: Player, velocity: Double) {
        val existing = bouncers[player]

        if (existing != null) {
            existing.updateVelocity(velocity)
            return
        }

        val bouncer = Bouncing(player, velocity)
        bouncers[player] = bouncer

        NeoForge.EVENT_BUS.register(bouncer)
    }

    fun removeBouncer(player: Player) {
        val bouncer = bouncers.remove(player) ?: return

        NeoForge.EVENT_BUS.unregister(bouncer)
    }

    fun isBouncing(player: Player): Boolean {
        return bouncers.containsKey(player)
    }

    private fun handlePlayerFall(player: LocalPlayer) {
        val isSneaking = player.isSuppressingBounce
        val isJumping = player.input.jumping && !isSneaking

        player.setDeltaMovement(
            player.deltaMovement.x * 1.05,
            player.deltaMovement.y * if (isJumping) -0.9 else -0.7,
            player.deltaMovement.z * 1.05
        )

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

        var motion = player.deltaMovement.y * if (isJumping) 0.5 else 1.0

        val bouncedOffSlime =
            player.level().getBlockState(player.blockPosition().below()).`is`(ModBlockTagsProvider.POGO_BOOST)
        if (!isSneaking && bouncedOffSlime) {
            motion *= 1.5
        }

        if (motion != 0.0) {
            addBouncer(player, motion)
            ModPacketHandler.messageServer(TellServerUsedPogo.INSTANCE)
        } else {
            removeBouncer(player)
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