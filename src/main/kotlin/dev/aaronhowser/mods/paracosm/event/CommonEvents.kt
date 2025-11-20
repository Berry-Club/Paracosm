package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.Delusion.Companion.delusion
import dev.aaronhowser.mods.paracosm.command.ModCommands
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.paracosm.entity.custom.AaronberryEntity
import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.entity.custom.StringWormEntity
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateRawWhimsyPacket
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(
	modid = Paracosm.ID
)
object CommonEvents {

	@SubscribeEvent
	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		ModPacketHandler.registerPayloads(event)
	}

	@SubscribeEvent
	fun entityAttributeEvent(event: EntityAttributeCreationEvent) {
		event.put(ModEntityTypes.TEDDY_BEAR.get(), TeddyBearEntity.setAttributes())
		event.put(ModEntityTypes.STRING_WORM.get(), StringWormEntity.setAttributes())
		event.put(ModEntityTypes.AARONBERRY.get(), AaronberryEntity.setAttributes())
	}

	@SubscribeEvent
	fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
		ModCommands.register(event.dispatcher)
	}

	@SubscribeEvent
	fun afterUseItem(event: LivingEntityUseItemEvent.Finish) {
		val entity = event.entity

		val food = event.item.getFoodProperties(entity)
		if (food != null) {

			if (event.item.`is`(ModItemTagsProvider.SWEETS)) {
				entity.whimsy += 0.5f
			}

		}
	}

	@SubscribeEvent
	fun onStartTracking(event: PlayerEvent.StartTracking) {
		val player = event.entity as? ServerPlayer ?: return
		val entity = event.target

		if (entity is LivingEntity) {
			if (entity.whimsy != 0f) {
				ModPacketHandler.messagePlayer(
					player,
					UpdateRawWhimsyPacket(
						entity.id,
						entity.whimsy,
						true
					)
				)
			}

			if (entity.delusion != 0f) {
				ModPacketHandler.messagePlayer(
					player,
					UpdateRawWhimsyPacket(
						entity.id,
						entity.delusion,
						false
					)
				)
			}
		}

		if (Upgradeable.getUpgrades(entity).isNotEmpty()) {
			ModPacketHandler.messagePlayer(
				player,
				UpdateEntityUpgrades(
					entity.id,
					Upgradeable.getUpgrades(entity).toList()
				)
			)
		}
	}

	@SubscribeEvent
	fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
		val player = event.entity as? ServerPlayer ?: return

		ModPacketHandler.messagePlayer(
			player,
			UpdateRawWhimsyPacket(
				player.id,
				player.whimsy,
				true
			)
		)

		ModPacketHandler.messagePlayer(
			player,
			UpdateRawWhimsyPacket(
				player.id,
				player.delusion,
				false
			)
		)
	}

	@SubscribeEvent
	fun onIncomingDamage(event: LivingIncomingDamageEvent) {
		PogoStickVehicle.checkCancelDamage(event)
	}

}