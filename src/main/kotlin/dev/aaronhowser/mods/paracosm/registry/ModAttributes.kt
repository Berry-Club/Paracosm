package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Paracosm.MOD_ID)

	val WHIMSY: DeferredHolder<Attribute, Attribute> =
		register(
			"living.whimsy",
			0.0, 0.0, Double.MAX_VALUE,
			sentiment = Attribute.Sentiment.POSITIVE
		)

	val DELUSION: DeferredHolder<Attribute, Attribute> =
		register(
			"living.delusion",
			0.0, 0.0, Double.MAX_VALUE,
			sentiment = Attribute.Sentiment.NEGATIVE
		)

	private fun register(
		name: String,
		default: Double,
		min: Double,
		max: Double,
		sync: Boolean = true,
		sentiment: Attribute.Sentiment = Attribute.Sentiment.NEUTRAL
	): DeferredHolder<Attribute, Attribute> {
		return ATTRIBUTE_REGISTRY.register(name, Supplier {
			val attribute = RangedAttribute(name, default, min, max)

			if (sync) {
				attribute.setSyncable(true)
			}

			attribute.setSentiment(sentiment)

			return@Supplier attribute
		})
	}

}