package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.*
import dev.aaronhowser.mods.paracosm.entity.projectile.DodgeballEntity
import dev.aaronhowser.mods.paracosm.entity.projectile.FoamDartProjectile
import dev.aaronhowser.mods.paracosm.entity.projectile.ShrinkRayProjectile
import dev.aaronhowser.mods.paracosm.entity.projectile.StickyHandProjectile
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntityTypes {

	val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Paracosm.MOD_ID)

	val TEDDY_BEAR: DeferredHolder<EntityType<*>, EntityType<TeddyBearEntity>> =
		register("teddy_bear", ::TeddyBearEntity, MobCategory.CREATURE, 0.5f, 1.1f)

	val STRING_WORM: DeferredHolder<EntityType<*>, EntityType<StringWormEntity>> =
		register("string_worm", ::StringWormEntity, MobCategory.CREATURE, 1f, 1f)

	val AARONBERRY: DeferredHolder<EntityType<*>, EntityType<AaronberryEntity>> =
		register("aaronberry", ::AaronberryEntity, MobCategory.CREATURE, 0.3f, 0.6f)

	val DODGEBALL: DeferredHolder<EntityType<*>, EntityType<DodgeballEntity>> =
		register("dodgeball", ::DodgeballEntity, MobCategory.MISC, 0.25f, 0.25f)

	val SHRINK_RAY_PROJECTILE: DeferredHolder<EntityType<*>, EntityType<ShrinkRayProjectile>> =
		register("shrink_ray_projectile", ::ShrinkRayProjectile, MobCategory.MISC, 0.25f, 0.25f)

	val STICKY_HAND_PROJECTILE: DeferredHolder<EntityType<*>, EntityType<StickyHandProjectile>> =
		register("sticky_hand_projectile", ::StickyHandProjectile, MobCategory.MISC, 0.25f, 0.25f)

	val POGO_STICK_VEHICLE: DeferredHolder<EntityType<*>, EntityType<PogoStickVehicle>> =
		register("pogo_stick", ::PogoStickVehicle, MobCategory.MISC, 0.5f, 1.5f)

	val FOAM_DART: DeferredHolder<EntityType<*>, EntityType<FoamDartProjectile>> =
		register("foam_dart", ::FoamDartProjectile, MobCategory.MISC, 0.25f, 0.25f)

	private fun <T : Entity> register(
		name: String,
		factory: EntityType.EntityFactory<T>,
		mobCategory: MobCategory,
		width: Float,
		height: Float
	): DeferredHolder<EntityType<*>, EntityType<T>> {
		return ENTITY_TYPE_REGISTRY.register(name, Supplier {
			EntityType.Builder.of(
				factory,
				mobCategory
			)
				.sized(width, height)
				.build(name)
		})
	}

}