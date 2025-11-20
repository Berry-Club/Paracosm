package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.entity.LivingEntity

class TemporaryWhimsy(
	val instances: MutableList<Instance>
) {

	fun tick(livingEntity: LivingEntity) {
		instances.removeIf(Instance::tick)
	}

	fun totalAmount(): Double {
		return instances
			.filterNot(Instance::isExpired)
			.sumOf(Instance::amount)
	}

	class Instance(
		val amount: Double,
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
						Codec.DOUBLE
							.fieldOf("amount")
							.forGetter(Instance::amount),
						Codec.INT
							.fieldOf("remaining_ticks")
							.forGetter(Instance::remainingTicks)
					).apply(instance, ::Instance)
				}
		}
	}

	companion object {
		val CODEC: Codec<TemporaryWhimsy> =
			Codec
				.list(Instance.CODEC)
				.xmap(::TemporaryWhimsy, TemporaryWhimsy::instances)
	}

}