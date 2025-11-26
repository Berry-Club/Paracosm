package dev.aaronhowser.mods.paracosm.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

open class FoamDartProjectile(
	entityType: EntityType<out FoamDartProjectile>,
	level: Level
) : Projectile(entityType, level) {

	protected var owner: LivingEntity? = null
	protected var pickupItemStack: ItemStack? = null

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		// none
	}

	override fun onHit(result: HitResult) {
		super.onHit(result)

		if (isServerSide) {
			if (pickupItemStack != null) {
				spawnAtLocation(pickupItemStack!!)
			}
			discard()
		}
	}

	override fun onHitBlock(result: BlockHitResult) {
		super.onHitBlock(result)

		val level = level()
		val blockPos = result.blockPos

		val blockState = level.getBlockState(blockPos)
		val block = blockState.block

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