package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.EitherCodec
import dev.aaronhowser.mods.aaron.AaronExtensions.isRight
import io.netty.buffer.ByteBuf
import net.minecraft.core.Position
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

data class ToySoldierDataComponent(
	val either: Either<ResourceLocation, CustomData>
) {

	constructor(entityType: EntityType<*>) : this(BuiltInRegistries.ENTITY_TYPE.getKey(entityType))
	constructor(location: ResourceLocation) : this(Either.left(location))
	constructor(data: CustomData) : this(Either.right(data))

	fun hasCustomData(): Boolean = either.isRight()

	fun placeEntity(level: Level, position: Position): Entity? {
		var result: Entity? = null

		either
			.ifLeft { location ->
				result = spawnFromType(level, position, location)
			}
			.ifRight { customData ->
				result = spawnFromData(level, position, customData)
			}

		return result
	}

	private fun spawnFromData(level: Level, position: Position, data: CustomData): Entity? {
		val typeLocation = ResourceLocation.parse(data.copyTag().getString("id"))
		val entity = getEntity(level, typeLocation) ?: return null

		data.loadInto(entity)

		entity.moveTo(position.x(), position.y(), position.z())
		level.addFreshEntity(entity)

		return entity
	}

	private fun spawnFromType(level: Level, position: Position, typeLocation: ResourceLocation): Entity? {
		val entity = getEntity(level, typeLocation) ?: return null

		entity.moveTo(position.x(), position.y(), position.z())
		level.addFreshEntity(entity)

		return entity
	}

	private fun getEntity(level: Level, typeRl: ResourceLocation): Entity? {
		return level.registryAccess()
			.registryOrThrow(Registries.ENTITY_TYPE)
			.get(typeRl)
			?.create(level)
	}

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

			stripData(nbt)

			val customData = CustomData.of(nbt)
			return ToySoldierDataComponent(customData)
		}

		private fun stripData(compoundTag: CompoundTag) {
			val badTags = listOf(
				"HurtByTimestamp",
				"Sitting",
				"FallFlying",
				"PortalCooldown",
				"FallDistance",
				"InLove",
				"DeathTime",
				"UUID",
				"Age",
				"ForcedAge",
				"Motion",
				"Air",
				"OnGround",
				"Rotation",
				"Pos",
				"HurtTime",
				"Owner"
			)

			for (tag in badTags) {
				compoundTag.remove(tag)
			}
		}
	}

}