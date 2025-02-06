package dev.aaronhowser.mods.paracosm.client

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.event.ClientTickEvent

object DuckHuntGunShaderHandler {

    private var heldGunLastTick = false

    //FIXME:
    // Shader ntsc_decode could not find sampler named BaseSampler in the specified shader program.
    // Shader color_convolve could not find uniform named InSize in the specified shader program.
    // Shader phosphor could not find uniform named InSize in the specified shader program.

    fun updateShader(event: ClientTickEvent.Pre) {
        val player = ClientUtil.localPlayer ?: return

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
        val shader = ResourceLocation.withDefaultNamespace("shaders/post/scan_pincushion.json")
        val renderer = Minecraft.getInstance().gameRenderer
        renderer.loadEffect(shader)
    }

    private fun disableShader() {
        Minecraft.getInstance().gameRenderer.shutdownEffect()
    }

}