package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.EitherCodec
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.registries.DeferredHolder

data class ToySoldierDataComponent(
	val either: Either<ResourceLocation, CustomData>
) {

	constructor(deferredEntityType: DeferredHolder<EntityType<*>, EntityType<*>>) : this(deferredEntityType.id)
	constructor(location: ResourceLocation) : this(Either.left(location))
	constructor(data: CustomData) : this(Either.right(data))

	companion object {
		val CODEC: Codec<ToySoldierDataComponent> =
			EitherCodec(ResourceLocation.CODEC, CustomData.CODEC)
				.xmap(::ToySoldierDataComponent, ToySoldierDataComponent::either)

		val STREAM_CODEC: StreamCodec<ByteBuf, ToySoldierDataComponent> =
			ByteBufCodecs.either(
				ResourceLocation.STREAM_CODEC,
				CustomData.STREAM_CODEC
			).map(::ToySoldierDataComponent, ToySoldierDataComponent::either)

		fun fromEntity(entity: LivingEntity): ToySoldierDataComponent {
			val nbt = CompoundTag()
			entity.save(nbt)

			val customData = CustomData.of(nbt)
			return ToySoldierDataComponent(customData)
		}
	}

}