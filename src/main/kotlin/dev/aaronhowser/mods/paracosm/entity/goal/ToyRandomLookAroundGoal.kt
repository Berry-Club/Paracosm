package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal

class ToyRandomLookAroundGoal(
    private val toyEntity: ToyEntity
) : RandomLookAroundGoal(toyEntity) {

    override fun canUse(): Boolean {
        if (this.toyEntity.isHiding) return false
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        if (this.toyEntity.isHiding) return false
        return super.canContinueToUse()
    }

}