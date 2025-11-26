package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.FoamDartProjectile
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.level.Level
import java.util.function.Predicate

class NerfGunItem(properties: Properties) : ProjectileWeaponItem(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val ammoStack = ModItems.FOAM_DART.toStack()
		val usedStack = player.getItemInHand(usedHand)

		if (level is ServerLevel) {
			shoot(
				level,
				player,
				usedHand,
				usedStack,
				listOf(ammoStack),
				3f,
				0f,
				false,
				null
			)
		}

		return InteractionResultHolder.success(usedStack)
	}

	override fun createProjectile(
		level: Level,
		shooter: LivingEntity,
		weapon: ItemStack,
		ammo: ItemStack,
		isCrit: Boolean
	): Projectile {
		if (ammo.item is FoamDartItem) {
			return FoamDartProjectile(shooter, ammo.copyWithCount(1), weapon)
		}

		return super.createProjectile(level, shooter, weapon, ammo, isCrit)
	}

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

	override fun getDefaultProjectileRange(): Int = 15

	@Deprecated("Deprecated in Java")
	override fun getAllSupportedProjectiles(): Predicate<ItemStack> {
		return Predicate { stack: ItemStack -> stack.`is`(ModItems.FOAM_DART) }
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties().stacksTo(1)
	}

}