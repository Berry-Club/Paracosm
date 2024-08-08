package dev.aaronhowser.mods.paracosm.entity.custom

import dev.aaronhowser.mods.paracosm.entity.base.ToyEntity
import dev.aaronhowser.mods.paracosm.entity.goal.FlopGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PlayerRideableJumping
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

open class StringWormEntity(
    entityType: EntityType<out TamableAnimal>,
    level: Level
) : ToyEntity(entityType, level), PlayerRideableJumping {

    override val requiredWhimsy: Float = 5f

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

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController(this, "controller", 0, this::predicate))
    }

    private fun predicate(animationState: AnimationState<StringWormEntity>): PlayState {
        val animationName = if (isHiding) {
            return PlayState.STOP
        } else if (animationState.isMoving) {
            "animation.stringworm.slither"
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

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        if (this.isVehicle || player.isSecondaryUseActive) return super.mobInteract(player, hand)

        val usedStack = player.getItemInHand(hand)

        if (!usedStack.isEmpty) {
            val itemUseResult = usedStack.interactLivingEntity(player, this, hand)

            if (itemUseResult.consumesAction()) return itemUseResult
        }

        this.doPlayerRide(player)
        return InteractionResult.sidedSuccess(level().isClientSide)
    }

    private fun doPlayerRide(player: Player) {
        if (level().isClientSide) return

        player.yRot = this.yRot
        player.xRot = this.xRot
        player.startRiding(this)
    }

    private var playerJumpPendingScale = 0f
    override fun onPlayerJump(jumpPower: Int) {
        val power = jumpPower.coerceAtLeast(0)

        playerJumpPendingScale = if (jumpPower > 90) {
            1f
        } else {
            0.4f + (0.4f * power / 90f)
        }
    }

    override fun canJump(): Boolean {
        return true
    }

    override fun handleStartJump(p0: Int) {
    }

    override fun handleStopJump() {
    }
}