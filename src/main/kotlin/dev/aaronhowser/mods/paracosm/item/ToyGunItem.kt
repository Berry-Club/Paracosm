package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.item.base.ToyItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class ToyGunItem : ToyItem(
    Properties()
        .stacksTo(1)
) {

    override val requiredWhimsy: Float = 5f

    override fun getName(stack: ItemStack): Component {
        return if (true /* Some value that changes depending on who's reading the tooltip */) {
            Component.literal("Super cool gun")
        } else {
            Component.literal("Toy gun")
        }
    }


}