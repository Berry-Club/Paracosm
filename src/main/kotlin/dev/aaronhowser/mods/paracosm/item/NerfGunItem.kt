package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileWeaponItem
import java.util.function.Predicate

class NerfGunItem(properties: Properties) : ProjectileWeaponItem(properties) {

	@Deprecated("Deprecated in Java")
	override fun getAllSupportedProjectiles(): Predicate<ItemStack> {
		return Predicate { stack: ItemStack -> stack.`is`(ModItems.FOAM_DART) }
	}

	override fun getDefaultProjectileRange(): Int = 15

	override fun shootProjectile(
		shooter: LivingEntity,
		projectile: Projectile,
		index: Int,
		velocity: Float,
		inaccuracy: Float,
		angle: Float,
		target: LivingEntity?
	) {
		projectile.shootFromRotation(
			shooter,
			shooter.xRot,
			shooter.yRot + angle,
			0f,
			velocity,
			inaccuracy
		)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties().stacksTo(1)
	}

}