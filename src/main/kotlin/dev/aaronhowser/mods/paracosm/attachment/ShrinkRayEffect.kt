package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

data class ShrinkRayEffect(
    val amount: Double
) {

    companion object {

        val CODEC: Codec<ShrinkRayEffect> =
            Codec.DOUBLE.xmap(::ShrinkRayEffect, ShrinkRayEffect::amount)

        var LivingEntity.shrinkRayEffect: Double
            get() = this.getData(ModAttachmentTypes.SHRINK_RAY_EFFECT).amount
            set(valueBad) {
                val value = valueBad.coerceIn(-0.9, 2.0)

                this.setData(ModAttachmentTypes.SHRINK_RAY_EFFECT, ShrinkRayEffect(value))

                val scaleAttribute = this.getAttribute(Attributes.SCALE) ?: return
                val modifier = AttributeModifier(
                    OtherUtil.modResource("shrink_ray_effect"),
                    shrinkRayEffect,
                    AttributeModifier.Operation.ADD_VALUE
                )

                scaleAttribute.addOrReplacePermanentModifier(modifier)

                if (!this.level().isClientSide) {
                    val nameString = this.name.string
                    Paracosm.LOGGER.debug("$nameString's Shrink Ray effect value is now $value")
                }
            }

    }
}