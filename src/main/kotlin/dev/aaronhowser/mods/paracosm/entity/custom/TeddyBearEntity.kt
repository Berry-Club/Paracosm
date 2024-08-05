package dev.aaronhowser.mods.paracosm.entity.custom

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.*
import java.util.*

class TeddyBearEntity(
    entityType: EntityType<out TamableAnimal>, level: Level
) : TamableAnimal(entityType, level), GeoEntity {

    companion object {

        fun setAttributes(): AttributeSupplier {
            return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.ATTACK_SPEED, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .build()
        }

    }

    override fun registerGoals() {
        this.goalSelector.let {
            it.addGoal(0, FloatGoal(this))
            it.addGoal(1, SitWhenOrderedToGoal(this))
            it.addGoal(3, WaterAvoidingRandomStrollGoal(this, 1.0))
            it.addGoal(4, LookAtPlayerGoal(this, Player::class.java, 6f))
            it.addGoal(5, RandomLookAroundGoal(this))
        }
    }

    override fun getBreedOffspring(p0: ServerLevel, p1: AgeableMob): AgeableMob? {
        return null
    }

    override fun isFood(p0: ItemStack): Boolean {
        return false
    }

    override fun getOwnerUUID(): UUID? {
        return null
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, "controller", 0, this::predicate))
    }

    private fun predicate(animationState: AnimationState<TeddyBearEntity>): PlayState {
        val animationName = if (animationState.isMoving) {
            "animation.teddybear.walk"
        } else {
            "animation.teddybear.flop"
        }

        animationState.controller.setAnimation(
            RawAnimation.begin().then(
                animationName,
                Animation.LoopType.LOOP
            )
        )

        return PlayState.CONTINUE
    }

    private val cache = SingletonAnimatableInstanceCache(this)

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return cache
    }
}