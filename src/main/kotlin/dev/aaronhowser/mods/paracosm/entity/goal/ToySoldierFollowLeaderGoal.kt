package dev.aaronhowser.mods.paracosm.entity.goal

import dev.aaronhowser.mods.paracosm.entity.base.ToySoldierEntity
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal

class ToySoldierFollowLeaderGoal(
	tamable: ToySoldierEntity,
	speedModifier: Double,
	startDistanceToPlayer: Float,
	stopDistanceToPlayer: Float,
	private val startDistanceToLeader: Float,
	private val stopDistanceToLeader: Float
) : FollowOwnerGoal(tamable, speedModifier, startDistanceToPlayer, stopDistanceToPlayer) {

	val toySoldier: ToySoldierEntity = tamable

	override fun canUse(): Boolean {
		if (toySoldier.isSquadLeader) {
			return super.canUse()
		}

		val leader = toySoldier.getSquadLeader() ?: return false

		if (toySoldier.unableToMoveToOwner() || toySoldier.closerThan(leader, startDistanceToLeader.toDouble())) {
			return false
		}

		this.owner = leader
		return true
	}

	override fun canContinueToUse(): Boolean {
		if (toySoldier.isSquadLeader) {
			return super.canContinueToUse()
		}

		val leader = toySoldier.getSquadLeader() ?: return false

		if (this.navigation.isDone || toySoldier.unableToMoveToOwner()) {
			return false
		}

		return !toySoldier.closerThan(leader, stopDistanceToLeader.toDouble())
	}

}