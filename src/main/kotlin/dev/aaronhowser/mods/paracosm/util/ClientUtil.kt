package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer

object ClientUtil {

	fun getLocalWhimsy(): Float = AaronClientUtil.localPlayer?.whimsy ?: 0f
	fun hasWhimsy(requiredWhimsy: Float): Boolean = getLocalWhimsy() >= requiredWhimsy

}