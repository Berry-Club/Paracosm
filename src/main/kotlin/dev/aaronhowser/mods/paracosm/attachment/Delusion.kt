package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.world.entity.LivingEntity

data class Delusion(
    val amount: Float
) {

    constructor() : this(0f)

    companion object {

        val CODEC: Codec<Delusion> =
            Codec.FLOAT.xmap(::Delusion, Delusion::amount)

        var LivingEntity.whimsy: Float
            get() = this.getData(ModAttachmentTypes.DELUSION).amount
            set(value) {
                this.setData(ModAttachmentTypes.DELUSION, Delusion(value))
            }

    }

}