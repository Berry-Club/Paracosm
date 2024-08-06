package dev.aaronhowser.mods.paracosm.item.base

import dev.aaronhowser.mods.paracosm.attachment.RequiresWhimsy
import net.minecraft.world.item.Item

abstract class ToyItem(
    properties: Properties
) : Item(properties), RequiresWhimsy