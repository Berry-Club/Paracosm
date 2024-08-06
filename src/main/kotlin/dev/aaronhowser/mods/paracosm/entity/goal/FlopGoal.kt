package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player

class FlopGoal(
    private val toyEntity: ToyEntity
) : Goal() {

    private val lowWhimsyObservers = mutableListOf<Player>()

    override fun canUse(): Boolean {
        if (!this.toyEntity.isTame) return false

        return toyEntity.isHiding
    }

    override fun canContinueToUse(): Boolean {
        return toyEntity.isHiding
    }

    override fun tick() {
        val newList = lowWhimsyObservers.filter { it.hasLineOfSight(this.toyEntity) }
        lowWhimsyObservers.clear()
        lowWhimsyObservers.addAll(newList)
    }

    override fun start() {
        this.toyEntity.isOrderedToSit = true
    }

    override fun stop() {
        this.toyEntity.isOrderedToSit = false
    }

}