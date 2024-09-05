package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class StringListComponent(
    val value: List<String>
) {

    companion object {
        val CODEC: Codec<StringListComponent> =
            Codec.STRING.listOf().xmap(::StringListComponent, StringListComponent::value)

        val STREAM_CODEC: StreamCodec<ByteBuf, StringListComponent> =
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(::StringListComponent, StringListComponent::value)
    }

    fun with(vararg newStrings: String): StringListComponent {
        return StringListComponent(value + newStrings)
    }

    fun without(vararg removeStrings: String): StringListComponent {
        return StringListComponent(value - removeStrings.toSet())
    }

}