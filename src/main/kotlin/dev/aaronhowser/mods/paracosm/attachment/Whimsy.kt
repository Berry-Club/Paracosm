package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.world.entity.LivingEntity

data class Whimsy(
    val amount: Float
) {

    constructor() : this(0f)

    companion object {

        val CODEC: Codec<Whimsy> =
            Codec.FLOAT.xmap(::Whimsy, Whimsy::amount)

        var LivingEntity.whimsy: Float
            get() = this.getData(ModAttachmentTypes.WHIMSY).amount
            set(value) {
                this.setData(ModAttachmentTypes.WHIMSY, Whimsy(value))
            }

    }

}