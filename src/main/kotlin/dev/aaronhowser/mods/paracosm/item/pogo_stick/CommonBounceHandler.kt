package dev.aaronhowser.mods.paracosm.item.pogo_stick

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.NeoForge

object CommonBounceHandler {

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

}