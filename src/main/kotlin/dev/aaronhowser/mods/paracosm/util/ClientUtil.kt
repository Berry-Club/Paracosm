package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

    val whimsy: Float
        get() = localPlayer?.whimsy ?: 0f

}