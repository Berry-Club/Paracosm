package dev.aaronhowser.mods.paracosm.item.pogo_stick

import net.minecraft.client.player.LocalPlayer
import net.neoforged.neoforge.event.tick.PlayerTickEvent

class Bouncing(
    val player: LocalPlayer,
    velocity: Double
) {

    init {
        updateVelocity(velocity)
    }

    var wasInAir = false
    var ticksOnGround = 0
    var pogoStickHeld = true
    var lastMotionX = 0.0
    var lastMotionZ = 0.0
    var verticalVelocity = 0.0
    var lastVelocityTicks = 0

    fun updateVelocity(verticalVelocity: Double) {
        if (verticalVelocity <= 0) return

        this.verticalVelocity = verticalVelocity
        this.lastVelocityTicks = player.tickCount
    }

    fun tick(event: PlayerTickEvent.Post) {
        val player = event.entity
        if (player.isFallFlying) return

        fun playerMovement() = this.player.deltaMovement

        if (this.verticalVelocity != 0.0) {

            if (this.player.tickCount == this.lastVelocityTicks) {
                this.player.setDeltaMovement(
                    playerMovement().x,
                    this.verticalVelocity,
                    playerMovement().z
                )
                this.lastVelocityTicks = 0
            }

            if (!(this.player.onGround() || this.lastMotionX == playerMovement().x && this.lastMotionZ == playerMovement().z)) {
                val a = 1.05

                this.player.setDeltaMovement(
                    playerMovement().x * a,
                    playerMovement().y,
                    playerMovement().z * a
                )

                this.lastMotionX = playerMovement().x
                this.lastMotionZ = playerMovement().z

                this.player.setOnGround(false)
            }
        }

        if (!this.wasInAir || !this.player.onGround()) {
            this.wasInAir = true
        }

        val onGround = this.player.onGround() || this.player.isInWater || this.player.abilities.flying

        if (onGround) {
            this.ticksOnGround++

            if (this.ticksOnGround > 1) {
                // JumpingHandler.removeProcess(this.player)
            }
        } else {
            this.ticksOnGround = 0
        }

    }

}