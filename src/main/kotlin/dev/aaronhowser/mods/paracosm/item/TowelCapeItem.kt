package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.getWhimsy
import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ElytraItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

class TowelCapeItem(properties: Properties) : IUpgradeableItem, RequiresWhimsy, ElytraItem(properties) {

	override val possibleUpgrades: List<String> = listOf(
		"cape_boost"
	)

	override val requiredWhimsy: Double = 10.0

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (level.isClientSide) return
		if (entity !is LivingEntity) return

		if (entity.onGround()) {
			stack.set(ModDataComponents.TOWEL_CAPE_WORKS, true)
		}
	}

	override fun canElytraFly(stack: ItemStack, entity: LivingEntity): Boolean {
		if (entity.getWhimsy() < requiredWhimsy) return false
		return stack.get(ModDataComponents.TOWEL_CAPE_WORKS) ?: false
	}

	override fun elytraFlightTick(stack: ItemStack, entity: LivingEntity, flightTicks: Int): Boolean {
		if (entity.isClientSide) return true

		if (flightTicks >= FLIGHT_DURATION) {
			stack.set(ModDataComponents.TOWEL_CAPE_WORKS, false)
			return false
		}

		val nextFlightTick = flightTicks + 1
		if (nextFlightTick % 10 == 0) {
			entity.gameEvent(GameEvent.ELYTRA_GLIDE)

			if (nextFlightTick % 20 == 0) {
				stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST)
			}
		}

		return true
	}

	companion object {
		private const val FLIGHT_DURATION = 20 * 5

		val DEFAULT_PROPERTIES: () -> Properties = 	{
			Properties()
				.durability(432)
				.rarity(Rarity.UNCOMMON)
				.component(
					ModDataComponents.TOWEL_CAPE_WORKS,
					true
				)
		}
	}

}