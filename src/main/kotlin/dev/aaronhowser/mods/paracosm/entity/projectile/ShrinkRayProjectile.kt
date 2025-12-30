package dev.aaronhowser.mods.paracosm.entity.projectile

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect.Companion.shrinkRayEffect
import dev.aaronhowser.mods.paracosm.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import kotlin.math.abs

class ShrinkRayProjectile(
	entityType: EntityType<ShrinkRayProjectile>,
	level: Level
) : RequiresWhimsy, Arrow(entityType, level) {

	constructor(shooter: ServerPlayer) : this(
		ModEntityTypes.SHRINK_RAY_PROJECTILE.get(),
		shooter.level()
	) {
		isGrow = shooter.isSecondaryUseActive

		moveTo(shooter.x, shooter.eyeY, shooter.z)

		owner = shooter
	}

	override val requiredWhimsy: Double = ModItems.SHRINK_RAY.get().requiredWhimsy

	var isGrow: Boolean
		get() = entityData.get(IS_GROW)
		set(value) = entityData.set(IS_GROW, value)

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		super.defineSynchedData(builder)
		builder.define(IS_GROW, false)
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		super.addAdditionalSaveData(compound)
		compound.putBoolean(IS_GROW_NBT, isGrow)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		super.readAdditionalSaveData(compound)
		isGrow = compound.getBoolean(IS_GROW_NBT)
	}

	override fun getDefaultPickupItem(): ItemStack {
		return ItemStack.EMPTY
	}

	override fun onHitBlock(result: BlockHitResult) {
		if (result.type == HitResult.Type.MISS) return

		val blockHit = level().getBlockState(result.blockPos)
		if (!blockHit.`is`(ModBlockTagsProvider.REFLECTIVE)) {
			discard()
			return
		}

		val directionNormal = result.direction.normal

		val newX = if (directionNormal.x != 0) -deltaMovement.x else deltaMovement.x
		val newY = if (directionNormal.y != 0) -deltaMovement.y else deltaMovement.y
		val newZ = if (directionNormal.z != 0) -deltaMovement.z else deltaMovement.z

		setDeltaMovement(newX, newY, newZ)

		lookAt(
			EntityAnchorArgument.Anchor.EYES,
			position().add(newX, newY, newZ)
		)

		age = 0
	}

	override fun onHitEntity(result: EntityHitResult) {
		val target = result.entity as? LivingEntity ?: return
		val amount = if (isGrow) 0.1 else -0.1
		val shooter = owner as? Player

		changeEntityScale(target, amount, shooter)

		discard()
	}

	private fun changeEntityScale(
		target: LivingEntity,
		scaleChange: Double,
		changer: Player? = null
	): Boolean {

		if (target is Player) {
			if (target.isSpectator) return false

			if (!hasEnoughWhimsy(target)) return false
		}

		val scaleBefore = target.getAttributeValue(Attributes.SCALE)

		target.shrinkRayEffect += scaleChange

		if (abs(target.shrinkRayEffect) < 0.01) {
			target.shrinkRayEffect = 0.0
		}

		val scaleAfter = target.getAttributeValue(Attributes.SCALE)

		val scaleChanged = scaleBefore != scaleAfter

		if (scaleChanged) {
			target.refreshDimensions()

			val entityName = target.name.string
			val afterString = "%.2f".format(scaleAfter)
			val changeMessage = Component.literal("$entityName scale effect changed to $afterString")

			changer?.displayClientMessage(changeMessage, true)

			if (changer != target && target is Player) {
				target.displayClientMessage(changeMessage, true)
			}

		}

		return scaleChanged
	}

	private var age = 0
	override fun tick() {
		super.tick()

		if (isHiding(level(), position())) {
			discard()
			return
		}

		age++
		if (age > 20 * 1) {
			discard()
		}
	}

	override fun getDefaultGravity(): Double {
		return 0.0
	}

	companion object {

		val IS_GROW: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(
				ShrinkRayProjectile::class.java,
				EntityDataSerializers.BOOLEAN
			)

		const val IS_GROW_NBT = "IsGrow"
	}

}