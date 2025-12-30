package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import java.util.*

abstract class ToyEntity(
	entityType: EntityType<out ToyEntity>,
	level: Level
) : LivingEntity(entityType, level), OwnableEntity, RequiresWhimsy, GeoEntity {

	var isHiding: Boolean = false
		private set

	override fun getOwnerUUID(): UUID? {
		return entityData.get(DATA_OWNER_UUID).orElse(null)
	}

	fun isOwnedBy(livingEntity: LivingEntity):

	override fun tick() {
		super.tick()
		isHiding = super.isHiding(level(), eyePosition)
	}

	fun hidingFromPlayers(): List<Player> {
		return super.hidingFromPlayers(level(), eyePosition)
	}

	companion object {
		val DATA_OWNER_UUID: EntityDataAccessor<Optional<UUID>> =
			SynchedEntityData.defineId(ToyEntity::class.java, EntityDataSerializers.OPTIONAL_UUID)


	}

}
