package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.command.ModCommands
import dev.aaronhowser.mods.paracosm.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.paracosm.entity.custom.AaronberryEntity
import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.entity.custom.StringWormEntity
import dev.aaronhowser.mods.paracosm.entity.custom.TeddyBearEntity
import dev.aaronhowser.mods.paracosm.handler.AttributeHandler.baseWhimsy
import dev.aaronhowser.mods.paracosm.packet.ModPacketHandler
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.registry.ModAttributes
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(
	modid = Paracosm.ID
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

		if (Upgradeable.getUpgrades(entity).isNotEmpty()) {
			val packet = UpdateEntityUpgrades(entity.id, Upgradeable.getUpgrades(entity).toList())
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
		val attacker = event.entity
		val newTarget = event.newAboutToBeSetTarget ?: return

		for (armorStack in newTarget.armorSlots) {
			val aggroImmuneFrom = armorStack.get(ModDataComponents.AGGRO_IMMUNE_FROM) ?: continue
			if (aggroImmuneFrom.any { attacker.type.`is`(it) }) {
				event.isCanceled = true
				return
			}
		}
	}

}