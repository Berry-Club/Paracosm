package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.status
import dev.aaronhowser.mods.paracosm.item.component.RotationalMomentumDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

class HulaHoopItem(properties: Properties) : Item(properties), ICurioItem {

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		super.curioTick(slotContext, stack)

		val entity = slotContext.entity
		updateMomentum(entity, stack)
	}

	override fun onUnequip(slotContext: SlotContext, newStack: ItemStack, stack: ItemStack) {
		super.onUnequip(slotContext, newStack, stack)

		stack.remove(ModDataComponents.ROTATIONAL_MOMENTUM)
	}

	companion object {
		private fun updateMomentum(entity: LivingEntity, stack: ItemStack) {
			val momentum = stack.get(ModDataComponents.ROTATIONAL_MOMENTUM)
			if (momentum == null) {
				initializeMomentum(entity, stack)
				return
			}

			val newMomentum = momentum.getWithNewPosition(entity.position())
			stack.set(ModDataComponents.ROTATIONAL_MOMENTUM, newMomentum)

			if (entity is Player) {
				entity.status("momentum: ${String.format("%.2f", newMomentum.clockwiseMomentum)} - direction: ${String.format("%.2f", newMomentum.previousDirection)}")
			}
		}

		private fun initializeMomentum(entity: LivingEntity, stack: ItemStack) {
			val component = RotationalMomentumDataComponent(
				clockwiseMomentum = 0.0,
				previousPosition = entity.position(),
				previousDirection = 0.0
			)

			stack.set(ModDataComponents.ROTATIONAL_MOMENTUM, component)
		}
	}

}