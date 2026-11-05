package dev.aaronhowser.mods.paracosm.registry

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
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Paracosm.MOD_ID)

	val SHRINK_RAY_EFFECT: DeferredHolder<AttachmentType<*>, AttachmentType<ShrinkRayEffect>> =
		register(
			"shrink_ray_effect",
			AttachmentType
				.builder(Supplier { ShrinkRayEffect(0.0) })
				.serialize(ShrinkRayEffect.CODEC)
				.sync(ShrinkRayEffect.STREAM_CODEC)
				.build()
		)

	val ENTITY_UPGRADES: DeferredHolder<AttachmentType<*>, AttachmentType<EntityUpgrades>> =
		register(
			"entity_upgrades",
			AttachmentType
				.builder(Supplier { EntityUpgrades() })
				.serialize(EntityUpgrades.CODEC)
				.build()
		)

	val RESEARCH_POINTS: DeferredHolder<AttachmentType<*>, AttachmentType<PlayerResearchPoints>> =
		register(
			"research_points",
			AttachmentType
				.builder(Supplier { PlayerResearchPoints(emptyMap()) })
				.serialize(PlayerResearchPoints.CODEC)
				.build()
		)

	private fun <T> register(name: String, type: AttachmentType<T>): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPES_REGISTRY.register(name, Supplier { type })
	}

}