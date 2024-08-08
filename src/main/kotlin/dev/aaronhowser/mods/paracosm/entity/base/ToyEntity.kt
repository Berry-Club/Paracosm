package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.util.OtherUtil
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
        return level().players().filter {
            !hasEnoughWhimsy(it)
                    && it.distanceToSqr(this) < 10.0 * 10.0
                    && OtherUtil.isLookingAtPos(it, eyePosition, 75f)
                    && it.hasLineOfSight(this)
        }
    }

    override fun getBreedOffspring(p0: ServerLevel, p1: AgeableMob): AgeableMob? {
        return null
    }

    override fun isFood(p0: ItemStack): Boolean {
        return false
    }

    override fun tick() {
        super.tick()
        isHiding = hidingFromPlayers().isNotEmpty()
    }

    var isHiding: Boolean = false
        private set

}
