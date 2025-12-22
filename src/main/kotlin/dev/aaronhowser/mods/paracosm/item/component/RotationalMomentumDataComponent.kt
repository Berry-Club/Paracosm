package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.toDegrees
import dev.aaronhowser.mods.aaron.AaronExtraCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import kotlin.math.atan2

data class RotationalMomentumDataComponent(
	val clockwiseMomentum: Double,
	val previousPosition: Vector2d,
	val previousDirection: Double
) {

	constructor(
		clockwiseMomentum: Double,
		previousPosition: Vec3,
		previousDirection: Double
	) : this(
		clockwiseMomentum,
		Vector2d(previousPosition.x, previousPosition.z),
		previousDirection
	)

	fun getWithNewPosition(newPosition: Vec3): RotationalMomentumDataComponent {
		val prevX = previousPosition.x
		val prevZ = previousPosition.y

		val deltaX = newPosition.x - prevX
		val deltaZ = newPosition.z - prevZ

		val newDirection = atan2(deltaZ, deltaX).toDegrees()
		val directionDifference = ((newDirection - previousDirection + 540) % 360) - 180

		if (directionDifference == 0.0 || directionDifference !in -90.0..90.0) {
			return RotationalMomentumDataComponent(
				clockwiseMomentum,
				Vector2d(newPosition.x, newPosition.z),
				newDirection
			)
		}

		val additionalMomentum = directionDifference * 0.1

		return RotationalMomentumDataComponent(
			clockwiseMomentum + additionalMomentum,
			Vector2d(newPosition.x, newPosition.z),
			newDirection
		)
	}

	companion object {
		val CODEC: Codec<RotationalMomentumDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.DOUBLE
						.fieldOf("momentum")
						.forGetter(RotationalMomentumDataComponent::clockwiseMomentum),
					AaronExtraCodecs.VECTOR2D_CODEC
						.fieldOf("position")
						.forGetter(RotationalMomentumDataComponent::previousPosition),
					Codec.DOUBLE
						.fieldOf("direction")
						.forGetter(RotationalMomentumDataComponent::previousDirection)
				).apply(instance, ::RotationalMomentumDataComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, RotationalMomentumDataComponent> =
			StreamCodec.composite(
				ByteBufCodecs.DOUBLE, RotationalMomentumDataComponent::clockwiseMomentum,
				AaronExtraCodecs.VECTOR2D_STREAM_CODEC, RotationalMomentumDataComponent::previousPosition,
				ByteBufCodecs.DOUBLE, RotationalMomentumDataComponent::previousDirection,
				::RotationalMomentumDataComponent
			)
	}

}