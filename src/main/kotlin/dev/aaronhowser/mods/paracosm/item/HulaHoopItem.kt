package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.status
import dev.aaronhowser.mods.aaron.AaronExtensions.tell
import dev.aaronhowser.mods.paracosm.item.component.AngularMomentumDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem
import java.util.*
import kotlin.math.absoluteValue

class HulaHoopItem(properties: Properties) : Item(properties), ICurioItem {

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		super.curioTick(slotContext, stack)

		val entity = slotContext.entity
		if (entity.isClientSide) return

		updateMomentum(entity, stack)
		bumpAwayEntities(entity, stack)
	}

	//FIXME: Not working?
	override fun onUnequip(slotContext: SlotContext, newStack: ItemStack, stack: ItemStack) {
		super.onUnequip(slotContext, newStack, stack)

		stack.remove(ModDataComponents.ANGULAR_MOMENTUM)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)

		private val LAST_BUMPED_AT = WeakHashMap<UUID, Long>()

		private fun updateMomentum(wearer: LivingEntity, hulaStack: ItemStack) {
			val momentum = hulaStack.get(ModDataComponents.ANGULAR_MOMENTUM)
			if (momentum == null) {
				val component = AngularMomentumDataComponent(
					counterclockwiseMomentum = 0.0,
					previousPosition = wearer.position(),
					previousDirection = 0.0
				)

				hulaStack.set(ModDataComponents.ANGULAR_MOMENTUM, component)
				return
			}

			val newMomentum = momentum.getWithNewPosition(wearer.position())
			hulaStack.set(ModDataComponents.ANGULAR_MOMENTUM, newMomentum)

			if (wearer is Player) {
				wearer.status("momentum: ${String.format("%.2f", newMomentum.counterclockwiseMomentum)} - direction: ${String.format("%.2f", newMomentum.previousDirection)}")
			}
		}

		private fun bumpAwayEntities(wearer: LivingEntity, hulaStack: ItemStack) {
			val entitiesNearby = wearer.level().getEntities(
				wearer,
				wearer.boundingBox.inflate(0.5, 0.5, 0.5)
			).filterIsInstance<LivingEntity>()

			val currentTick = wearer.level().gameTime

			for (entity in entitiesNearby) {
				val lastBumpedAt = LAST_BUMPED_AT[entity.uuid] ?: 0L
				if (currentTick - lastBumpedAt < 20L) {
					continue
				}

				val momentum = hulaStack.get(ModDataComponents.ANGULAR_MOMENTUM) ?: return
				if (momentum.counterclockwiseMomentum.absoluteValue < 1.0) return

				LAST_BUMPED_AT[entity.uuid] = currentTick

				val pushDirection = wearer
					.position()
					.vectorTo(entity.position())
					.with(Direction.Axis.Y, 0.0)
					.normalize()
					.with(Direction.Axis.Y, 0.5)

				val pushAmount = momentum.counterclockwiseMomentum.absoluteValue

				val pushVec = pushDirection.scale(pushAmount)
				entity.addDeltaMovement(pushVec)

				val newMomentum = momentum.getWithLessMomentum(1.0)
				hulaStack.set(ModDataComponents.ANGULAR_MOMENTUM, newMomentum)

				//TODO: Play a thunk sound?

				wearer.tell("Bumped ${entity.name.string}!")
			}
		}
	}

}