package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.getWhimsy

object ClientUtil {

	fun getLocalWhimsy(): Double = AaronClientUtil.localPlayer?.getWhimsy() ?: 0.0
	fun hasWhimsy(requiredWhimsy: Double): Boolean = getLocalWhimsy() >= requiredWhimsy

}