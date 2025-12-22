package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3

data class RotationalMomentumDataComponent(
	val clockwiseMomentum: Double,
	val previousPosition: Pair<Double, Double>
) {

	constructor(
		clockwiseMomentum: Double,
		previousPosition: Vec3
	) : this(
		clockwiseMomentum,
		Pair(previousPosition.x, previousPosition.z)
	)

	companion object {
		val CODEC: Codec<RotationalMomentumDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.DOUBLE
						.fieldOf("clockwise_momentum")
						.forGetter(RotationalMomentumDataComponent::clockwiseMomentum),
					Codec.pair(Codec.DOUBLE, Codec.DOUBLE)
						.fieldOf("previous_position")
						.forGetter(RotationalMomentumDataComponent::previousPosition)
				).apply(instance, ::RotationalMomentumDataComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, RotationalMomentumDataComponent> =
			object : StreamCodec<ByteBuf, RotationalMomentumDataComponent> {
				override fun encode(buffer: ByteBuf, value: RotationalMomentumDataComponent) {
					buffer.writeDouble(value.clockwiseMomentum)
					buffer.writeDouble(value.previousPosition.first)
					buffer.writeDouble(value.previousPosition.second)
				}

				override fun decode(buffer: ByteBuf): RotationalMomentumDataComponent {
					val clockwiseMomentum = buffer.readDouble()
					val previousX = buffer.readDouble()
					val previousZ = buffer.readDouble()
					return RotationalMomentumDataComponent(
						clockwiseMomentum,
						Pair(previousX, previousZ)
					)
				}
			}
	}

}