package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.player.Player

class ToyLookAtPlayerGoal(
    private val toyEntity: ToyEntity,
    lookDistance: Float,
    probability: Float
) : LookAtPlayerGoal(toyEntity, Player::class.java, lookDistance, probability) {

    constructor(toyEntity: ToyEntity, lookDistance: Float) : this(toyEntity, lookDistance, 0.02f)
    constructor(toyEntity: ToyEntity) : this(toyEntity, 6f)

    override fun canUse(): Boolean {
        if (this.toyEntity.isOrderedToSit) return false
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        if (this.toyEntity.isOrderedToSit) return false
        return super.canContinueToUse()
    }

}