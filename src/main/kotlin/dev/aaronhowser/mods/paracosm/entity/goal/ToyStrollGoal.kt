package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal

class ToyStrollGoal(
	private val toy: ToyEntity,
	speedModifier: Double
) : WaterAvoidingRandomStrollGoal(toy, speedModifier) {

	override fun canUse(): Boolean {
		return !toy.isHiding && super.canUse()
	}

	override fun canContinueToUse(): Boolean {
		return !toy.isHiding && super.canContinueToUse()
	}

}