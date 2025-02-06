package dev.aaronhowser.mods.paracosm.client

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.event.tick.PlayerTickEvent

object DuckHuntGunShaderHandler {

    private var heldGunLastTick = false

    fun updateShader(event: PlayerTickEvent.Pre) {
        val player = event.entity

        val holdingGun = player.isHolding(ModItems.DUCK_HUNT_GUN.get())
        if (holdingGun == heldGunLastTick) return
        heldGunLastTick = holdingGun

        if (holdingGun) {
            enableShader()
        } else {
            disableShader()
        }
    }

    private fun enableShader() {
        val shader = ResourceLocation.withDefaultNamespace("shaders/program/invert.json")

        Minecraft.getInstance().gameRenderer.loadEffect(shader)
    }

    private fun disableShader() {
        Minecraft.getInstance().gameRenderer.shutdownEffect()
    }

}