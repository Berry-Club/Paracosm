package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ElytraItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

class TowelCapeItem : RequiresWhimsy, ElytraItem(
    Properties()
        .durability(432)
        .rarity(Rarity.UNCOMMON)
) {

    override val requiredWhimsy: Float = 10f
    override val hasCustomModelHandling: Boolean = false

    override fun canElytraFly(stack: ItemStack, entity: LivingEntity): Boolean {
        return super.canElytraFly(stack, entity)
    }

}