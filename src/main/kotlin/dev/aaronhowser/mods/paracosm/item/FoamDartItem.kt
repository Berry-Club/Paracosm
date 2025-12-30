package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.projectile.FoamDartProjectile
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

class FoamDartItem(properties: Properties) : Item(properties), ProjectileItem {

	override fun asProjectile(level: Level, pos: Position, stack: ItemStack, direction: Direction): Projectile {
		val dart = FoamDartProjectile(level, pos, stack.copyWithCount(1), null)
		return dart
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
	}

}