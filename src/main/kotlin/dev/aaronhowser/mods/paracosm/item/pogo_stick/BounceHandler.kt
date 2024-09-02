package dev.aaronhowser.mods.paracosm.item.pogo_stick

import dev.aaronhowser.mods.paracosm.item.PogoStickItem
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.NeoForge

object BounceHandler {

    private val bouncers: MutableMap<LocalPlayer, Bouncing> = mutableMapOf()

    fun addBouncer(player: LocalPlayer, velocity: Double) {
        val existing = bouncers[player]

        if (existing != null) {
            existing.updateVelocity(velocity)
            return
        }

        val bouncer = Bouncing(player, velocity)
        bouncers[player] = bouncer

        NeoForge.EVENT_BUS.register(bouncer)
    }

    fun removeBouncer(player: LocalPlayer) {
        val bouncer = bouncers.remove(player) ?: return

        NeoForge.EVENT_BUS.unregister(bouncer)
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

        //TODO: Block tag for extra bouncy blocks?
        val bouncedOffSlime = player.level().getBlockState(player.blockPosition().below()).block == Blocks.SLIME_BLOCK
        if (!isSneaking && bouncedOffSlime) {
            motion *= 1.5
        }

        if (motion != 0.0) {
            addBouncer(player, motion)
            //TODO: Packet
        } else {
            removeBouncer(player)
        }

    }

    private fun shouldPlayerBounce(player: LocalPlayer, distance: Double): Boolean {
        return distance >= 1 && PogoStickItem.getHeldPogoStick(player) != null
    }

}