package dev.aaronhowser.mods.paracosm.entity

import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.phys.BlockHitResult

open class FoamDartProjectile(
	entityType: EntityType<out FoamDartProjectile>,
	level: Level
) : Projectile(entityType, level) {

	private var owner: LivingEntity? = null

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		// none
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

*/