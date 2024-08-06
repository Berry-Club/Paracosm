package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ElytraItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

class TowelCapeItem : RequiresWhimsy, ElytraItem(
    Properties()
        .durability(432)
        .rarity(Rarity.UNCOMMON)
        .component(
            ModDataComponents.TOWEL_CAPE_TIME_LEFT,
            5 * 20
        )
) {

    override val requiredWhimsy: Float = 10f
    override val hasCustomModelHandling: Boolean = false

    override fun canElytraFly(stack: ItemStack, entity: LivingEntity): Boolean {
        val timeLeft = stack.get(ModDataComponents.TOWEL_CAPE_TIME_LEFT) ?: 0
        return timeLeft > 0
    }

    override fun elytraFlightTick(stack: ItemStack, entity: LivingEntity, flightTicks: Int): Boolean {
        if (entity.level().isClientSide) return true

        return flightTicks <= 40
    }

}