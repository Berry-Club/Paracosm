package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateShrinkRayScale
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import dev.aaronhowser.mods.paracosm.util.OtherUtil.isClientSide
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

data class ShrinkRayEffect(
    val amount: Double
) {

    companion object {

        val CODEC: Codec<ShrinkRayEffect> =
            Codec.DOUBLE.xmap(::ShrinkRayEffect, ShrinkRayEffect::amount)

        val attributeModifierId = OtherUtil.modResource("shrink_ray_effect")

        var LivingEntity.shrinkRayEffect: Double
            get() = this.getData(ModAttachmentTypes.SHRINK_RAY_EFFECT).amount
            set(valueUnCoerced) {
                val value = valueUnCoerced.coerceIn(-0.9, 2.0)

                if (!this.isClientSide) {
                    ModPacketHandler.messageAllPlayers(
                        UpdateShrinkRayScale(
                            this.id,
                            value
                        )
                    )
                }

                this.setData(ModAttachmentTypes.SHRINK_RAY_EFFECT, ShrinkRayEffect(value))

                val scaleAttribute = this.getAttribute(Attributes.SCALE) ?: return
                val modifier = AttributeModifier(
                    attributeModifierId,
                    shrinkRayEffect,
                    AttributeModifier.Operation.ADD_VALUE
                )

                scaleAttribute.addOrReplacePermanentModifier(modifier)

                if (value == 0.0) {
                    scaleAttribute.removeModifier(modifier)
                }
            }

    }
}