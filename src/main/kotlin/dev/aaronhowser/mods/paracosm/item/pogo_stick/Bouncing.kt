package dev.aaronhowser.mods.paracosm.item.pogo_stick

import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

class Bouncing(
    val player: Player,
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

    @SubscribeEvent
    fun tick(event: PlayerTickEvent.Post) {
        val player = event.entity
        if (player.isFallFlying) return

        if (this.verticalVelocity != 0.0) {
            whileInAir()
        }

        if (!this.wasInAir || !this.player.onGround()) {
            this.wasInAir = true
        }

        val onGround = this.player.onGround() || this.player.isInWater || this.player.abilities.flying

        if (onGround) {
            this.ticksOnGround++

            if (this.ticksOnGround > 1) {
                CommonBounceHandler.removeBouncer(this.player)
            }
        } else {
            this.ticksOnGround = 0
        }

    }

    private fun whileInAir() {
        fun currentDelta() = this.player.deltaMovement

        if (this.player.tickCount == this.lastVelocityTicks) {
            this.player.setDeltaMovement(
                currentDelta().x,
                this.verticalVelocity,
                currentDelta().z
            )
            this.lastVelocityTicks = 0
        }

        if (!(this.player.onGround() || this.lastMotionX == currentDelta().x && this.lastMotionZ == currentDelta().z)) {
            val horizontalAcceleration = 1.05

            this.player.setDeltaMovement(
                currentDelta().x * horizontalAcceleration,
                currentDelta().y,
                currentDelta().z * horizontalAcceleration
            )

            this.lastMotionX = currentDelta().x
            this.lastMotionZ = currentDelta().z

            this.player.setOnGround(false)  //FIXME: This is technically supposed to be isInAir = true or something, but that doesn't exist now
        }
    }

}