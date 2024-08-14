package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.Delusion
import dev.aaronhowser.mods.paracosm.attachment.ShrinkRayEffect
import dev.aaronhowser.mods.paracosm.attachment.Whimsy
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

    val ATTACHMENT_TYPES_REGISTRY: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Paracosm.ID)

    val WHIMSY: DeferredHolder<AttachmentType<*>, AttachmentType<Whimsy>> =
        ATTACHMENT_TYPES_REGISTRY.register("whimsy", Supplier {
            AttachmentType
                .builder(Supplier { Whimsy() })
                .serialize(Whimsy.CODEC)
                .copyOnDeath()
                .build()
        })

    val DELUSION: DeferredHolder<AttachmentType<*>, AttachmentType<Delusion>> =
        ATTACHMENT_TYPES_REGISTRY.register("delusion", Supplier {
            AttachmentType
                .builder(Supplier { Delusion() })
                .serialize(Delusion.CODEC)
                .copyOnDeath()
                .build()
        })

    val SHRINK_RAY_EFFECT: DeferredHolder<AttachmentType<*>, AttachmentType<ShrinkRayEffect>> =
        ATTACHMENT_TYPES_REGISTRY.register("shrink_ray_effect", Supplier {
            AttachmentType
                .builder(Supplier { ShrinkRayEffect(0.0) })
                .serialize(ShrinkRayEffect.CODEC)
                .copyOnDeath()
                .build()
        })

}