package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.core.Position
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

open class FoamDartProjectile : AbstractArrow {

	constructor(
		entityType: EntityType<out FoamDartProjectile>,
		level: Level
	) : super(entityType, level)

	constructor(
		level: Level,
		position: Position,
		pickupStack: ItemStack,
		firedFromWeapon: ItemStack?
	) : super(
		ModEntityTypes.FOAM_DART.get(),
		position.x(),
		position.y(),
		position.z(),
		level,
		pickupStack,
		firedFromWeapon
	)

	constructor(
		owner: LivingEntity,
		pickupStack: ItemStack,
		firedFromWeapon: ItemStack?
	) : super(
		ModEntityTypes.FOAM_DART.get(),
		owner,
		owner.level(),
		pickupStack,
		firedFromWeapon
	)

	override fun onHit(result: HitResult) {
		super.onHit(result)

		if (isServerSide) {
			spawnAtLocation(pickupItem, 0.1f)
			discard()
		}
	}

	override fun onHitBlock(result: BlockHitResult) {
		super.onHitBlock(result)

		val level = level()
		val blockPos = result.blockPos

		val blockState = level.getBlockState(blockPos)
		val block = blockState.block

		val owner = getOwner()

		if (block is ButtonBlock) {
			block.press(blockState, level, blockPos, owner as? Player)
		}

		if (block is DoorBlock) {
			block.setOpen(
				owner,
				level,
				blockState,
				blockPos,
				!blockState.getValue(DoorBlock.OPEN)
			)
		}
	}

	override fun getDefaultPickupItem(): ItemStack = ModItems.FOAM_DART.toStack()

}

/*  Ideas and junk

Knockback
Punching glove?

Flashbang
Blinds everybody nearby
Could instead be a confetti dart that does the same but is more whimsical?

Firecracker
Makes mobs run away? should that actually just be like firework snappers?

Smoke cloud?

Homing? Richochet?

BANG flag that's not actually a dart and just refills your whimsy or whatever

Suction cup dart type?
Sticks into blocks and entities like an arrow
Could be the base of some other upgrades?

Weighted suction dart
Slows down entities
Gives blocks gravity?

*/