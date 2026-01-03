package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronMobEffectsRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.neoforged.neoforge.registries.DeferredRegister

object ModMobEffects : AaronMobEffectsRegistry() {

	val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
		DeferredRegister.create(Registries.MOB_EFFECT, Paracosm.MOD_ID)

	override fun getMobEffectRegistry(): DeferredRegister<MobEffect> = EFFECT_REGISTRY

	val APHANTASIA =
		register("aphantasia") {
			MobEffect(MobEffectCategory.HARMFUL, 0x111111)
				.addAttributeModifier(
					ModAttributes.WHIMSY,
					Paracosm.modResource("aphantasia"),
					-0.25,
					AttributeModifier.Operation.ADD_MULTIPLIED_BASE
				)
		}

}