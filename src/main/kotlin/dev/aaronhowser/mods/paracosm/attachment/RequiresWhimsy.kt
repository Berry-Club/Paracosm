package dev.aaronhowser.mods.paracosm.attachment

import dev.aaronhowser.mods.paracosm.config.ServerConfig
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.getWhimsy
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.OtherUtil.hasLineOfSight
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

interface RequiresWhimsy {

	val requiredWhimsy: Double

	fun hasEnoughWhimsy(observerEntity: LivingEntity): Boolean {
		return (observerEntity.getWhimsy()) >= requiredWhimsy
	}

	fun hidingFromPlayers(level: Level, vec3: Vec3): List<Player> {
		return level.players().filter {
			!hasEnoughWhimsy(it)
					&& it.distanceToSqr(vec3) < ServerConfig.CONFIG.toyFlopRange.get().pow(2)
					&& OtherUtil.isLookingAtPos(it, vec3, 75f)
					&& it.hasLineOfSight(level, vec3)
		}
	}

	fun isHiding(level: Level, vec3: Vec3): Boolean {
		return hidingFromPlayers(level, vec3).isNotEmpty()
	}

}