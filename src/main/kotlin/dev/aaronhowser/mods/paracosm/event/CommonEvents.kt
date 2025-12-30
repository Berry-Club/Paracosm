package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.command.ModCommands
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.paracosm.entity.*
import dev.aaronhowser.mods.paracosm.entity.base.IUpgradeableEntity
import dev.aaronhowser.mods.paracosm.handler.ArmorHandler
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.baseWhimsy
import dev.aaronhowser.mods.paracosm.handler.KeyHandler
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(
	modid = Paracosm.MOD_ID
)
object CommonEvents {

	@SubscribeEvent
	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		ModPacketHandler.registerPayloads(event)
	}

	@SubscribeEvent
	fun onEntityAttributeModification(event: EntityAttributeModificationEvent) {
		val types = event.types

		for (entityType in types) {
			if (!event.has(entityType, ModAttributes.WHIMSY)) {
				event.add(entityType, ModAttributes.WHIMSY)
			}

			if (!event.has(entityType, ModAttributes.DELUSION)) {
				event.add(entityType, ModAttributes.DELUSION)
			}
		}
	}

	@SubscribeEvent
	fun entityAttributeEvent(event: EntityAttributeCreationEvent) {
		event.put(ModEntityTypes.TEDDY_BEAR.get(), TeddyBearEntity.setAttributes())
		event.put(ModEntityTypes.STRING_WORM.get(), StringWormEntity.setAttributes())
		event.put(ModEntityTypes.AARONBERRY.get(), AaronberryEntity.setAttributes())
		event.put(ModEntityTypes.TOY_SOLDIER_GUNNER.get(), ToySoldierGunnerEntity.setAttributes())
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
				entity.baseWhimsy += 0.5f
			}
		}
	}

	@SubscribeEvent
	fun onStartTracking(event: PlayerEvent.StartTracking) {
		val player = event.entity as? ServerPlayer ?: return
		val entity = event.target

		if (IUpgradeableEntity.getUpgrades(entity).isNotEmpty()) {
			val packet = UpdateEntityUpgrades(entity.id, IUpgradeableEntity.getUpgrades(entity).toList())
			packet.messagePlayer(player)
		}
	}

	@SubscribeEvent
	fun onIncomingDamage(event: LivingIncomingDamageEvent) {
		PogoStickVehicle.checkCancelDamage(event)
	}

	@SubscribeEvent
	fun onNewDataPackRegistry(event: DataPackRegistryEvent.NewRegistry) {
		event.dataPackRegistry(
			ModResearchTypes.RESEARCH_TYPE_RK,
			ResearchType.DIRECT_CODEC,
			ResearchType.DIRECT_CODEC
		)
	}

	@SubscribeEvent
	fun onLivingChangeTarget(event: LivingChangeTargetEvent) {
		ArmorHandler.stopAggro(event)
	}

	@SubscribeEvent
	fun onChangeEquipment(event: LivingEquipmentChangeEvent) {
		ArmorHandler.stopAggro(event)
	}

	@SubscribeEvent
	fun onPlayerLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
		KeyHandler.remove(event.entity)
	}

	@SubscribeEvent
	fun onPlayerChangeDimension(event: PlayerEvent.PlayerChangedDimensionEvent) {
		KeyHandler.remove(event.entity)
	}

}