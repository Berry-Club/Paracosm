package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.custom.*
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
                ::TeddyBearEntity,
                MobCategory.CREATURE
            )
                .sized(0.5f, 1.1f)
                .build("teddy_bear")
        })

    val STRING_WORM: DeferredHolder<EntityType<*>, EntityType<StringWormEntity>> =
        ENTITY_TYPE_REGISTRY.register("string_worm", Supplier {
            EntityType.Builder.of(
                ::StringWormEntity,
                MobCategory.CREATURE
            )
                .sized(1f, 1f)
                .build("string_worm")
        })

    val AARONBERRY: DeferredHolder<EntityType<*>, EntityType<AaronberryEntity>> =
        ENTITY_TYPE_REGISTRY.register("aaronberry", Supplier {
            EntityType.Builder.of(
                ::AaronberryEntity,
                MobCategory.CREATURE
            )
                .sized(0.3f, 0.6f)
                .build("aaronberry")
        })

    val DODGEBALL: DeferredHolder<EntityType<*>, EntityType<DodgeballEntity>> =
        ENTITY_TYPE_REGISTRY.register("dodgeball", Supplier {
            EntityType.Builder.of(
                ::DodgeballEntity,
                MobCategory.MISC
            )
                .sized(0.25f, 0.25f)
                .build("dodgeball")
        })

    val SHRINK_RAY_PROJECTILE: DeferredHolder<EntityType<*>, EntityType<ShrinkRayProjectile>> =
        ENTITY_TYPE_REGISTRY.register("shrink_ray_projectile", Supplier {
            EntityType.Builder.of(
                ::ShrinkRayProjectile,
                MobCategory.MISC
            )
                .sized(0.25f, 0.25f)
                .build("shrink_ray_projectile")
        })

    val STICKY_HAND_PROJECTILE: DeferredHolder<EntityType<*>, EntityType<StickyHandProjectile>> =
        ENTITY_TYPE_REGISTRY.register("sticky_hand_projectile", Supplier {
            EntityType.Builder.of(
                ::StickyHandProjectile,
                MobCategory.MISC
            )
                .sized(0.25f, 0.25f)
                .build("sticky_hand_projectile")
        })

    val POGO_STICK_VEHICLE: DeferredHolder<EntityType<*>, EntityType<PogoStickVehicle>> =
        ENTITY_TYPE_REGISTRY.register("pogo_stick_vehicle", Supplier {
            EntityType.Builder.of(
                ::PogoStickVehicle,
                MobCategory.MISC
            )
                .sized(0.5f, 1.5f)
                .build("pogo_stick_vehicle")
        })

}