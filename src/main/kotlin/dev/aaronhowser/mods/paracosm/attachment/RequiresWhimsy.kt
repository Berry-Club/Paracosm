package dev.aaronhowser.mods.paracosm.attachment

import dev.aaronhowser.mods.paracosm.attachment.Whimsy.Companion.whimsy
import net.minecraft.world.entity.LivingEntity

interface RequiresWhimsy {

    val requiredWhimsy: Float

    fun hasEnoughWhimsy(otherEntity: LivingEntity): Boolean {
        return (otherEntity.whimsy) >= requiredWhimsy
    }

}