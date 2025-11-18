package dev.aaronhowser.mods.paracosm.util

import com.google.common.collect.HashMultimap
import dev.aaronhowser.mods.paracosm.Paracosm
import kotlinx.coroutines.Runnable
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent

@EventBusSubscriber(
	modid = Paracosm.ID
)
object ModServerScheduler {

	@SubscribeEvent
	fun onServerTick(event: ServerTickEvent.Post) {
		handleScheduledTasks()
		currentTick++
	}

	var currentTick = 0
		private set

	private val upcomingTasks: HashMultimap<Int, Runnable> = HashMultimap.create()

	fun scheduleTaskInTicks(ticksInFuture: Int, task: Runnable) {
		if (ticksInFuture == 0) {
			task.run()
		} else {
			upcomingTasks.put(currentTick + ticksInFuture, task)
		}
	}

	private fun handleScheduledTasks() {
		val taskIterator = upcomingTasks[currentTick].iterator()

		while (taskIterator.hasNext()) {
			try {
				taskIterator.next().run()
			} catch (e: Exception) {
				e.printStackTrace()
			}

			taskIterator.remove()
		}
	}

}