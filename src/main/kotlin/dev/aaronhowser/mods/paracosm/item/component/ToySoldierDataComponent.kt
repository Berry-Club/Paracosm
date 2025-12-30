package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Position
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import java.util.*

data class ToySoldierDataComponent(
	val type: EntityType<*>,
	val data: Optional<CustomData>
) {

	constructor(entityType: EntityType<*>) : this(entityType, Optional.empty())

	fun hasCustomData(): Boolean = data.isPresent

	fun placeEntity(level: Level, position: Position): Entity? {
		val entity = type.create(level) ?: return null

		data.ifPresent { nbt ->
			nbt.loadInto(entity)
		}

		entity.moveTo(position.x(), position.y(), position.z())
		level.addFreshEntity(entity)

		return entity
	}

	companion object {
		val CODEC: Codec<ToySoldierDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					BuiltInRegistries.ENTITY_TYPE.byNameCodec()
						.fieldOf("type")
						.forGetter(ToySoldierDataComponent::type),
					CustomData.CODEC
						.optionalFieldOf("data")
						.forGetter(ToySoldierDataComponent::data)
				).apply(instance, ::ToySoldierDataComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, ToySoldierDataComponent> =
			StreamCodec.composite(
				ByteBufCodecs.fromCodec(BuiltInRegistries.ENTITY_TYPE.byNameCodec()), ToySoldierDataComponent::type,
				ByteBufCodecs.optional(CustomData.STREAM_CODEC), ToySoldierDataComponent::data,
				::ToySoldierDataComponent
			)

		fun fromEntity(entity: LivingEntity): ToySoldierDataComponent {
			val nbt = CompoundTag()
			entity.save(nbt)

			stripData(nbt)

			val customData = CustomData.of(nbt)
			return ToySoldierDataComponent(entity.type, Optional.of(customData))
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