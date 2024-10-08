package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyStrollGoal
import dev.aaronhowser.mods.paracosm.registry.ModSounds
import dev.aaronhowser.mods.paracosm.util.OtherUtil.isClientSide
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache
import software.bernie.geckolib.animation.*

class TeddyBearEntity(
    entityType: EntityType<TeddyBearEntity>,
    level: Level
) : ToyEntity(entityType, level) {

    override val requiredWhimsy: Float = 10f

    companion object {

        fun setAttributes(): AttributeSupplier {
            return Mob.createMobAttributes()
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
            it.addGoal(2, SitWhenOrderedToGoal(this))
            it.addGoal(3, ToyStrollGoal(this, 1.0))
            it.addGoal(4, ToyLookAtPlayerGoal(this))
            it.addGoal(5, ToyRandomLookAroundGoal(this))
        }
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {

        if (!this.isClientSide && hand == InteractionHand.MAIN_HAND) {
            this.level().playSound(
                this,
                this.blockPosition(),
                ModSounds.SQUEE.get(),
                this.soundSource,
                1f,
                1f
            )
        }

        if (!isTame) {
            tame(player)
            level().broadcastEntityEvent(this, 7)
        }

        if (hand == InteractionHand.MAIN_HAND && !player.isClientSide && isHiding) {

            val component = Component.literal("I can't move because ")
                .append(Component.literal("[these players]").withStyle { style ->

                    val playerComponents = hidingFromPlayers().map { player ->
                        Component.literal(player.gameProfile.name)
                    }

                    style.withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            CommonComponents.joinLines(playerComponents)
                        )
                    )
                })
                .append(Component.literal(" are looking at me and don't believe!"))

            player.sendSystemMessage(component)
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED
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