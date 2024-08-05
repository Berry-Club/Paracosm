package dev.aaronhowser.mods.paracosm.attachment

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.client.Minecraft

interface RequiresWhimsy {

    val requiredWhimsy: Float

    fun hasEnough(): Boolean {
        return (Minecraft.getInstance().player?.whimsy ?: 0f) >= requiredWhimsy
    }

}