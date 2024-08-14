package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import dev.aaronhowser.mods.paracosm.util.VariableComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ToyGunItem : RequiresWhimsy, Item(
    Properties()
        .stacksTo(1)
) {

    override val requiredWhimsy: Float = 5f

    private val nameComponent = VariableComponent(
        Component.literal("Super cool gun"),
        Component.literal("Toy gun")
    ) { ClientUtil.whimsy >= requiredWhimsy }

    override fun getName(stack: ItemStack): Component {
        return nameComponent
    }


}