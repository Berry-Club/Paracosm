package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

data class RotationalMomentumDataComponent(
	val clockwiseMomentum: Double,
	val previousPosition: Pair<Double, Double>,
	val previousDirection: Double
) {

	constructor(
		clockwiseMomentum: Double,
		previousPosition: Vec3,
		previousDirection: Double
	) : this(
		clockwiseMomentum,
		Pair(previousPosition.x, previousPosition.z),
		previousDirection
	)

	fun getWithNewPosition(newPosition: Vec3): RotationalMomentumDataComponent {
		val prevX = previousPosition.first
		val prevZ = previousPosition.second

		val deltaX = newPosition.x - prevX
		val deltaZ = newPosition.z - prevZ

		val newDirection = Math.toDegrees(atan2(deltaZ, deltaX))
		val directionDifference = ((newDirection - previousDirection + 540) % 360) - 180

		if (directionDifference == 0.0) {
			return RotationalMomentumDataComponent(
				clockwiseMomentum,
				Pair(newPosition.x, newPosition.z),
				newDirection
			)
		}

		val additionalMomentum = directionDifference * 0.1

		return RotationalMomentumDataComponent(
			clockwiseMomentum + additionalMomentum,
			Pair(newPosition.x, newPosition.z),
			newDirection
		)
	}

	companion object {
		val CODEC: Codec<RotationalMomentumDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.DOUBLE
						.fieldOf("clockwise_momentum")
						.forGetter(RotationalMomentumDataComponent::clockwiseMomentum),
					Codec.pair(Codec.DOUBLE, Codec.DOUBLE)
						.fieldOf("previous_position")
						.forGetter(RotationalMomentumDataComponent::previousPosition),
					Codec.DOUBLE
						.fieldOf("previous_direction")
						.forGetter(RotationalMomentumDataComponent::previousDirection)
				).apply(instance, ::RotationalMomentumDataComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, RotationalMomentumDataComponent> =
			object : StreamCodec<ByteBuf, RotationalMomentumDataComponent> {
				override fun encode(buffer: ByteBuf, value: RotationalMomentumDataComponent) {
					buffer.writeDouble(value.clockwiseMomentum)
					buffer.writeDouble(value.previousPosition.first)
					buffer.writeDouble(value.previousPosition.second)
					buffer.writeDouble(value.previousDirection)
				}

				override fun decode(buffer: ByteBuf): RotationalMomentumDataComponent {
					val clockwiseMomentum = buffer.readDouble()
					val previousX = buffer.readDouble()
					val previousZ = buffer.readDouble()
					val previousDirection = buffer.readDouble()
					return RotationalMomentumDataComponent(
						clockwiseMomentum,
						Pair(previousX, previousZ),
						previousDirection
					)
				}
			}
	}

}