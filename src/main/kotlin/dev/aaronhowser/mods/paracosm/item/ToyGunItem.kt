package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ToyGunItem : RequiresWhimsy, Item(
    Properties()
        .stacksTo(1)
) {

    override val requiredWhimsy: Float = 5f
    override val hasCustomModelHandling: Boolean = true

    override fun getName(stack: ItemStack): Component {
        return if (ClientUtil.whimsy >= requiredWhimsy) {
            Component.literal("Super cool gun")
        } else {
            Component.literal("Toy gun")
        }
    }


}