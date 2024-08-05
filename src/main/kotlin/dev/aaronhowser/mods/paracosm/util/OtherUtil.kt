package dev.aaronhowser.mods.paracosm.util

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.resources.ResourceLocation

object OtherUtil {

    fun modResource(path: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(Paracosm.ID, path)

}