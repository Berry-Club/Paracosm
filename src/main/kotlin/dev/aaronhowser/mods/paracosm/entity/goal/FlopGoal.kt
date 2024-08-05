package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player

class FlopGoal(
    private val livingEntity: ToyEntity
) : Goal() {

    private val lowWhimsyObservers = mutableListOf<Player>()

    override fun canUse(): Boolean {
        if (!this.livingEntity.isTame) return false

        val badPlayers = this.livingEntity.level().players().filter {
            it.distanceToSqr(this.livingEntity) < 10.0 * 10.0
                    && it.whimsy < this.livingEntity.requiredWhimsy
                    && it.hasLineOfSight(this.livingEntity)
        }

        lowWhimsyObservers.clear()
        lowWhimsyObservers.addAll(badPlayers)

        return lowWhimsyObservers.isNotEmpty()
    }

    override fun canContinueToUse(): Boolean {
        return lowWhimsyObservers.isNotEmpty()
    }

    override fun tick() {
        val newList = lowWhimsyObservers.filter { it.hasLineOfSight(this.livingEntity) }
        lowWhimsyObservers.clear()
        lowWhimsyObservers.addAll(newList)
    }

    override fun start() {
        this.livingEntity.isOrderedToSit = true
    }

    override fun stop() {
        this.livingEntity.isOrderedToSit = false
    }

}