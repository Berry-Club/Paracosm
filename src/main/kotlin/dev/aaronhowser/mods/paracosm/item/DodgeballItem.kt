package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.custom.DodgeballEntity
import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

class DodgeballItem(properties: Properties) : Item(properties), IUpgradeableItem, ProjectileItem {

	override val possibleUpgrades: List<String> = emptyList()

	override fun asProjectile(
		level: Level,
		position: Position,
		stack: ItemStack,
		direction: Direction
	): Projectile {
		val dodgeBallEntity = DodgeballEntity(level, position.x(), position.y(), position.z())
		dodgeBallEntity.item = stack

		return dodgeBallEntity
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val heldStack = player.getItemInHand(usedHand)

		if (level.isClientSide) return InteractionResultHolder.pass(heldStack)

		val dodgeBallEntity = DodgeballEntity(level, player)
		dodgeBallEntity.item = heldStack
		dodgeBallEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 1.5f, 1.0f)

		level.addFreshEntity(dodgeBallEntity)

		return InteractionResultHolder.success(heldStack)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}