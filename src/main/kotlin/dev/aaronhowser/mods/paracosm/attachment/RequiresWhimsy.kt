package dev.aaronhowser.mods.paracosm.attachment

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.world.entity.LivingEntity

interface RequiresWhimsy {

    val requiredWhimsy: Float

    val hasCustomModelHandling: Boolean

    fun canBeSeenReal(otherEntity: LivingEntity): Boolean {
        return (otherEntity.whimsy) >= requiredWhimsy
    }

}