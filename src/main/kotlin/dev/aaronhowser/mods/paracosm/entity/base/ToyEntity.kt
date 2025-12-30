package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity

abstract class ToyEntity(
	entityType: EntityType<out TamableAnimal>,
	level: Level
) : TamableAnimal(entityType, level), RequiresWhimsy, GeoEntity {

	fun hidingFromPlayers(): List<Player> {
		return super.hidingFromPlayers(level(), eyePosition)
	}

	override fun getBreedOffspring(p0: ServerLevel, p1: AgeableMob): AgeableMob? {
		return null
	}

	override fun isFood(p0: ItemStack): Boolean {
		return false
	}

	override fun tick() {
		super.tick()
		isHiding = super.isHiding(level(), eyePosition)
	}

	var isHiding: Boolean = false
		private set

}
