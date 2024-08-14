package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModSounds {

    val SOUND_EVENT_REGISTRY: DeferredRegister<SoundEvent> =
        DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Paracosm.ID)

    val DODGEBALL: DeferredHolder<SoundEvent, SoundEvent> =
        SOUND_EVENT_REGISTRY.register("dodgeball", Supplier {
            SoundEvent.createVariableRangeEvent(OtherUtil.modResource("dodgeball"))
        })

    val FART: DeferredHolder<SoundEvent, SoundEvent> =
        SOUND_EVENT_REGISTRY.register("fart", Supplier {
            SoundEvent.createVariableRangeEvent(OtherUtil.modResource("fart"))
        })

    val UNFART: DeferredHolder<SoundEvent, SoundEvent> =
        SOUND_EVENT_REGISTRY.register("unfart", Supplier {
            SoundEvent.createVariableRangeEvent(OtherUtil.modResource("unfart"))
        })

    val SQUEE: DeferredHolder<SoundEvent, SoundEvent> =
        SOUND_EVENT_REGISTRY.register("squee", Supplier {
            SoundEvent.createVariableRangeEvent(OtherUtil.modResource("squee"))
        })

}