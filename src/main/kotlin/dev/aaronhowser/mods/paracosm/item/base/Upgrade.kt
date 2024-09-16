package dev.aaronhowser.mods.paracosm.item.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

data class Upgrade(
    val id: ResourceLocation,
    val validItemTypes: List<Item>
) {

    val translationKey = "${id.namespace}.paracosm_upgrade.${id.path}"

    companion object {

        val CODEC: Codec<Upgrade> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(Upgrade::id),
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("validItemTypes")
                        .forGetter(Upgrade::validItemTypes)
                ).apply(instance, ::Upgrade)
            }

        val STREAM_CODEC: StreamCodec<ByteBuf, Upgrade> =
            ByteBufCodecs.fromCodec(CODEC)

    }

}