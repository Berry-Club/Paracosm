package dev.aaronhowser.mods.paracosm.entity.base

import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import java.util.*

abstract class ToySoldierEntity(
	entityType: EntityType<out ToySoldierEntity>,
	level: Level
) : ToyEntity(entityType, level) {

	var squadLeaderUuid: UUID? = null
	val isSquadLeader: Boolean get() = squadLeaderUuid == null

	fun getSquadLeader(): ToySoldierEntity? {
		if (isSquadLeader) return this

		val leaderUuid = squadLeaderUuid ?: return null
		val nearbySoldiers = level().getEntities(this, boundingBox.inflate(40.0)).filterIsInstance<ToySoldierEntity>()
		return nearbySoldiers.firstOrNull { it.uuid == leaderUuid }
	}

	fun setSquadLeader(toySoldierEntity: ToySoldierEntity?): Boolean {
		if (toySoldierEntity == null) {
			squadLeaderUuid = null
			return true
		}

		if (toySoldierEntity == this) return false
		if (!toySoldierEntity.isSquadLeader) return false
		squadLeaderUuid = toySoldierEntity.uuid
		return true
	}

	fun getChildren(): List<ToySoldierEntity> {
		if (!isSquadLeader) return emptyList()

		val nearbySoldiers = level().getEntities(this, boundingBox.inflate(40.0)).filterIsInstance<ToySoldierEntity>()
		return nearbySoldiers.filter { it.squadLeaderUuid == this.uuid }
	}

	fun getSquadMembers(): List<ToySoldierEntity> {
		val leader = getSquadLeader() ?: return emptyList()

		return leader.getChildren() + leader
	}

}