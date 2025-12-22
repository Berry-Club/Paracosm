package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.toDegrees
import dev.aaronhowser.mods.aaron.AaronExtraCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import kotlin.math.abs
import kotlin.math.atan2

data class RotationalMomentumDataComponent(
	val counterclockwiseMomentum: Double,
	val previousPosition: Vector2d,
	val previousDirection: Double
) {

	constructor(
		counterclockwiseMomentum: Double,
		previousPosition: Vec3,
		previousDirection: Double
	) : this(
		counterclockwiseMomentum,
		Vector2d(previousPosition.x, previousPosition.z),
		previousDirection
	)

	fun getWithNewPosition(newPosition: Vec3): RotationalMomentumDataComponent {
		if (newPosition == previousPosition) {
			return bleedMomentum(newPosition, previousDirection)
		}

		val prevX = previousPosition.x
		val prevZ = previousPosition.y

		val deltaX = newPosition.x - prevX
		val deltaZ = newPosition.z - prevZ

		val newDirection = atan2(deltaZ, deltaX).toDegrees()
		val directionDifference = Mth.degreesDifference(newDirection.toFloat(), previousDirection.toFloat())

		if (directionDifference in -1.0..1.0 || directionDifference !in -90.0..90.0) {
			return bleedMomentum(newPosition, newDirection)
		}

		val additionalMomentum = directionDifference * 0.1

		return RotationalMomentumDataComponent(
			counterclockwiseMomentum + additionalMomentum,
			Vector2d(newPosition.x, newPosition.z),
			newDirection
		)
	}

	private fun bleedMomentum(newPosition: Vec3, newDirection: Double): RotationalMomentumDataComponent {
		var newMomentum = counterclockwiseMomentum * 0.99
		if (abs(newMomentum) < 0.1) {
			newMomentum = 0.0
		}

		return RotationalMomentumDataComponent(
			newMomentum,
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
						.forGetter(RotationalMomentumDataComponent::counterclockwiseMomentum),
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
				ByteBufCodecs.DOUBLE, RotationalMomentumDataComponent::counterclockwiseMomentum,
				AaronExtraCodecs.VECTOR2D_STREAM_CODEC, RotationalMomentumDataComponent::previousPosition,
				ByteBufCodecs.DOUBLE, RotationalMomentumDataComponent::previousDirection,
				::RotationalMomentumDataComponent
			)
	}

}