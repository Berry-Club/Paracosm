package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.status
import dev.aaronhowser.mods.paracosm.item.component.AngularMomentumDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem
import kotlin.math.absoluteValue

class HulaHoopItem(properties: Properties) : Item(properties), ICurioItem {

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		super.curioTick(slotContext, stack)

		val entity = slotContext.entity
		updateMomentum(entity, stack)
	}

	//FIXME: Not working?
	override fun onUnequip(slotContext: SlotContext, newStack: ItemStack, stack: ItemStack) {
		super.onUnequip(slotContext, newStack, stack)

		stack.remove(ModDataComponents.ANGULAR_MOMENTUM)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)

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
			val momentum = hulaStack.get(ModDataComponents.ANGULAR_MOMENTUM) ?: return
			if (momentum.counterclockwiseMomentum.absoluteValue < 0.1) return
		}
	}

}