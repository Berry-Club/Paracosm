package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.custom.DodgeballEntity
import dev.aaronhowser.mods.paracosm.entity.custom.StringWormEntity
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntityTypes {

    val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Paracosm.ID)

    val TEDDY_BEAR: DeferredHolder<EntityType<*>, EntityType<TeddyBearEntity>> =
        ENTITY_TYPE_REGISTRY.register("teddy_bear", Supplier {
            EntityType.Builder.of(
                { type, level -> TeddyBearEntity(type, level) },
                MobCategory.MISC
            )
                .sized(1f, 1f)
                .build("teddy_bear")
        })

    val STRING_WORM: DeferredHolder<EntityType<*>, EntityType<StringWormEntity>> =
        ENTITY_TYPE_REGISTRY.register("string_worm", Supplier {
            EntityType.Builder.of(
                { type, level -> StringWormEntity(type, level) },
                MobCategory.MISC
            )
                .sized(1f, 1f)
                .build("string_worm")
        })

    val DODGEBALL: DeferredHolder<EntityType<*>, EntityType<DodgeballEntity>> =
        ENTITY_TYPE_REGISTRY.register("dodgeball", Supplier {
            EntityType.Builder.of(
                { type, level -> DodgeballEntity(type, level) },
                MobCategory.MISC
            )
                .sized(0.25f, 0.25f)
                .build("dodgeball")
        })

}