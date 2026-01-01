package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal

class ToySoldierFollowLeaderGoal(
	tamable: TamableAnimal,
	speedModifier: Double,
	startDistanceToPlayer: Float,
	stopDistanceToPlayer: Float,
	private val startDistanceToLeader: Float,
	private val stopDistanceToLeader: Float
) : FollowOwnerGoal(tamable, speedModifier, startDistanceToPlayer, stopDistanceToPlayer) {

	override fun canUse(): Boolean {
		val me = this.tamable as? ToySoldierEntity

		if (me == null || me.isSquadLeader) {
			return super.canUse()
		}

		val leader = me.getSquadLeader() ?: return false

		if (me.unableToMoveToOwner() || me.closerThan(leader, startDistanceToLeader.toDouble())) {
			return false
		}

		this.owner = leader
		return true
	}

}