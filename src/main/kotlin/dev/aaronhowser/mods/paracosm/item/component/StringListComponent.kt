package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class StringListComponent(
    val value: Set<String>
) {

    constructor(list: List<String>) : this(list.toSet())
    constructor() : this(emptySet())

    companion object {
        val CODEC: Codec<StringListComponent> =
            Codec.STRING.listOf().xmap(::StringListComponent) { it.value.toList() }

        val STREAM_CODEC: StreamCodec<ByteBuf, StringListComponent> =
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(::StringListComponent) { it.value.toList() }
    }

    fun with(vararg newStrings: String): StringListComponent {
        return StringListComponent((value + newStrings).toSet())
    }

    fun without(vararg removeStrings: String): StringListComponent {
        return StringListComponent(value - removeStrings.toSet())
    }

}