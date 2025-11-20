package dev.aaronhowser.mods.paracosm.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.EntityUpgrades
import dev.aaronhowser.mods.paracosm.attachment.PlayerResearchPoints
import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

	val ATTACHMENT_TYPES_REGISTRY: DeferredRegister<AttachmentType<*>> =
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Paracosm.ID)

	val SHRINK_RAY_EFFECT: DeferredHolder<AttachmentType<*>, AttachmentType<ShrinkRayEffect>> =
		register("shrink_ray_effect", { ShrinkRayEffect(0.0) }, ShrinkRayEffect.CODEC)

	val ENTITY_UPGRADES: DeferredHolder<AttachmentType<*>, AttachmentType<EntityUpgrades>> =
		register("entity_upgrades", { EntityUpgrades() }, EntityUpgrades.CODEC)

	val RESEARCH_POINTS =
		register("research_points", { PlayerResearchPoints(emptyMap()) }, PlayerResearchPoints.CODEC)

	private fun <T> register(
		name: String,
		builder: Supplier<T>,
		codec: Codec<T>,
		copyOnDeath: Boolean = true
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPES_REGISTRY.register(name, Supplier {
			val typeBuilder = AttachmentType
				.builder(builder)
				.serialize(codec)

			if (copyOnDeath) {
				typeBuilder.copyOnDeath()
			}

			typeBuilder.build()
		})
	}

}