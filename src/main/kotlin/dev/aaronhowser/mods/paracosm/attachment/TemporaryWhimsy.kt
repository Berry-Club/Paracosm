package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.LivingEntity

class TemporaryWhimsy(
	val instances: MutableList<Instance>
) {

	class Instance(
		val amount: Float,
		var remainingTicks: Int
	) {
		fun isExpired(): Boolean = remainingTicks <= 0

		/** @return true if expired */
		fun tick(): Boolean {
			remainingTicks--
			return isExpired()
		}

		companion object {
			val CODEC: Codec<Instance> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Codec.FLOAT
							.fieldOf("amount")
							.forGetter(Instance::amount),
						Codec.INT
							.fieldOf("remaining_ticks")
							.forGetter(Instance::remainingTicks)
					).apply(instance, ::Instance)
				}
		}
	}

	fun tick(livingEntity: LivingEntity) {
		instances.removeIf(Instance::tick)
	}

	companion object {
		val CODEC: Codec<TemporaryWhimsy> =
			Codec
				.list(Instance.CODEC)
				.xmap(::TemporaryWhimsy, TemporaryWhimsy::instances)
	}

}