package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.custom.StickyHandProjectile
import dev.aaronhowser.mods.paracosm.registry.ModSounds
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

//TODO: Alternate upgrade that makes it PUNCH
class StickyHandItem : Item(
	Properties()
		.stacksTo(1)
) {

	companion object {
		val playerStickyHands = mutableMapOf<Player, StickyHandProjectile>()
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val heldItem = player.getItemInHand(usedHand)

		val playerHand = playerStickyHands[player]

		if (playerHand == null) {
			level.playSound(    //TODO: new sound (and another sound for landing? wet thwap)
				null,
				player.x,
				player.y,
				player.z,
				ModSounds.STICKY_HAND_THROW.get(),
				SoundSource.NEUTRAL,
				0.5f,
				0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f)
			)

			if (level is ServerLevel) {
				val stickyHand = StickyHandProjectile(player)

				playerStickyHands[player] = stickyHand
				level.addFreshEntity(stickyHand)
			}
		} else {

			level.playSound(
				null,
				player.x,
				player.y,
				player.z,
				ModSounds.STICKY_HAND_RETRIEVE.get(),
				SoundSource.NEUTRAL,
				0.5f,
				0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f)
			)

			if (level is ServerLevel) {
				playerStickyHands.remove(player)
				playerHand.retrieve()
			}

		}


		return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide)
	}

}