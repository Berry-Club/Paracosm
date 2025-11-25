package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.StickyHandProjectile
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
class StickyHandItem(properties: Properties) : Item(properties) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val heldItem = player.getItemInHand(usedHand)

		val playerHand = PLAYER_STICKY_HANDS[player]

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

				PLAYER_STICKY_HANDS[player] = stickyHand
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
				PLAYER_STICKY_HANDS.remove(player)
				playerHand.retrieve()
			}

		}


		return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide)
	}

	companion object {
		val PLAYER_STICKY_HANDS: MutableMap<Player, StickyHandProjectile> = mutableMapOf()
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}