package dev.aaronhowser.mods.paracosm.advancement

import dev.aaronhowser.mods.paracosm.block.NightLightBlock
import dev.aaronhowser.mods.paracosm.datagen.ModAdvancementSubProvider
import dev.aaronhowser.mods.paracosm.registry.ModBlocks
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent

object AdvancementHandler {

	private fun completeAdvancement(player: ServerPlayer, advancement: AdvancementHolder) {
		val progress = player.advancements.getOrStartProgress(advancement)
		if (progress.isDone) return

		val criteria = progress.remainingCriteria.iterator()

		while (criteria.hasNext()) {
			val criterion = criteria.next()
			player.advancements.award(advancement, criterion)
		}
	}

	fun hasCompletedAdvancement(player: ServerPlayer, advancement: AdvancementHolder): Boolean {
		val progress = player.advancements.getOrStartProgress(advancement)
		return progress.isDone
	}

	// TODO: Maybe use a mixin in ServerPlayer.stopSleeping?
	fun afterWakeUp(event: SleepFinishedTimeEvent) {
		val level = event.level as? ServerLevel ?: return

		val sleepingPlayers = level.players().filter(Player::isSleeping)
		if (sleepingPlayers.isEmpty()) return

		nightLight(level, sleepingPlayers)
	}

	private fun nightLight(level: ServerLevel, sleepingPlayers: List<ServerPlayer>) {
		val playersSleepingInDark = sleepingPlayers
			.filter { player ->
				level.getRawBrightness(player.blockPosition().above(), 0) < 5
			}
			.toList()

		val advancement = level.server.advancements.get(ModAdvancementSubProvider.Companion.SLEEP_WITH_NIGHT_LIGHT) ?: return

		for (player in playersSleepingInDark) {
			if (hasCompletedAdvancement(player, advancement)) continue

			val playerPos = player.blockPosition()
			val nearbyBlocks = BlockPos.betweenClosedStream(
				playerPos.offset(-10, -2, -10),
				playerPos.offset(10, 2, 10)
			)

			posLoop@
			for (pos in nearbyBlocks) {
				val state = level.getBlockState(pos)
				if (!state.`is`(ModBlocks.NIGHT_LIGHT)) continue

				if (state.getValue(NightLightBlock.Companion.ENABLED)) {
					completeAdvancement(player, advancement)
					break@posLoop
				}
			}
		}
	}

}