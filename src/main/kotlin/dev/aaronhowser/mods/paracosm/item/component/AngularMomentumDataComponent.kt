package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.toDegrees
import dev.aaronhowser.mods.aaron.AaronExtraCodecs
import dev.aaronhowser.mods.paracosm.config.ServerConfig
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sign

data class AngularMomentumDataComponent(
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

	//TODO: Make it not jump when changing from not moving to moving (in certain direction) and vice versa
	fun getWithNewPosition(newPosition: Vec3): AngularMomentumDataComponent {
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

		val additionalMomentum = directionDifference / 360.0 * ServerConfig.CONFIG.hulaHoopMomentumPerRotation.get()

		return AngularMomentumDataComponent(
			counterclockwiseMomentum + additionalMomentum,
			Vector2d(newPosition.x, newPosition.z),
			newDirection
		)
	}

	private fun bleedMomentum(newPosition: Vec3, newDirection: Double): AngularMomentumDataComponent {
		val bleedAmount = ServerConfig.CONFIG.hulaHoopBleedPerTick.get()
		val amountToRemove = bleedAmount * counterclockwiseMomentum.sign

		var newMomentum = counterclockwiseMomentum - amountToRemove

		if (abs(newMomentum) < bleedAmount) {
			newMomentum = 0.0
		}

		return AngularMomentumDataComponent(
			newMomentum,
			Vector2d(newPosition.x, newPosition.z),
			newDirection
		)
	}

	fun getWithLessMomentum(amount: Double): AngularMomentumDataComponent {
		var newMomentum = counterclockwiseMomentum - amount * counterclockwiseMomentum.sign
		if (abs(newMomentum) < ServerConfig.CONFIG.hulaHoopBleedPerTick.get()) {
			newMomentum = 0.0
		}

		return AngularMomentumDataComponent(
			newMomentum,
			previousPosition,
			previousDirection
		)
	}

	companion object {
		val CODEC: Codec<AngularMomentumDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.DOUBLE
						.fieldOf("momentum")
						.forGetter(AngularMomentumDataComponent::counterclockwiseMomentum),
					AaronExtraCodecs.VECTOR2D_CODEC
						.fieldOf("position")
						.forGetter(AngularMomentumDataComponent::previousPosition),
					Codec.DOUBLE
						.fieldOf("direction")
						.forGetter(AngularMomentumDataComponent::previousDirection)
				).apply(instance, ::AngularMomentumDataComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, AngularMomentumDataComponent> =
			StreamCodec.composite(
				ByteBufCodecs.DOUBLE, AngularMomentumDataComponent::counterclockwiseMomentum,
				AaronExtraCodecs.VECTOR2D_STREAM_CODEC, AngularMomentumDataComponent::previousPosition,
				ByteBufCodecs.DOUBLE, AngularMomentumDataComponent::previousDirection,
				::AngularMomentumDataComponent
			)
	}

}