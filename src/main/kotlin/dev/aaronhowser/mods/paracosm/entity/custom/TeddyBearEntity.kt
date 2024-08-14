package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import dev.aaronhowser.mods.paracosm.entity.goal.FlopGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.*

class TeddyBearEntity(
    entityType: EntityType<out TamableAnimal>,
    level: Level
) : ToyEntity(entityType, level) {

    override val requiredWhimsy: Float = 10f

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
            it.addGoal(1, FlopGoal(this))
            it.addGoal(2, SitWhenOrderedToGoal(this))
            it.addGoal(3, WaterAvoidingRandomStrollGoal(this, 1.0))
            it.addGoal(4, ToyLookAtPlayerGoal(this))
            it.addGoal(5, ToyRandomLookAroundGoal(this))
        }
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        if (!isTame) {
            tame(player)
            level().broadcastEntityEvent(this, 7)

            return InteractionResult.SUCCESS_NO_ITEM_USED
        }

        if (hand == InteractionHand.MAIN_HAND && !player.level().isClientSide && isHiding) {

            val component = Component.literal("I can't move because ")
                .append(Component.literal("[these players]").withStyle { style ->
                    style.withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.literal(hidingFromPlayers().joinToString("\n") { it.gameProfile.name })
                        )
                    )
                })
                .append(Component.literal(" are looking at me and don't believe!"))

            player.sendSystemMessage(component)

            return InteractionResult.PASS
        }

        return InteractionResult.PASS
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, "controller", 0, this::predicate))
    }

    private fun predicate(animationState: AnimationState<TeddyBearEntity>): PlayState {
        val animationName = if (isHiding) {
            "animation.teddybear.flop"
        } else if (animationState.isMoving) {
            "animation.teddybear.walk"
        } else return PlayState.STOP

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