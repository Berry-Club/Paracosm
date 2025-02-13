package dev.aaronhowser.mods.paracosm.event

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.client.render.entity.*
import dev.aaronhowser.mods.paracosm.client.render.layer.TowelCapeLayer
import dev.aaronhowser.mods.paracosm.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.paracosm.item.ToyGunItem
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.ClientUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.client.renderer.entity.layers.ElytraLayer
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.ModelEvent

@EventBusSubscriber(
    modid = Paracosm.ID,
    bus = EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object ClientModBusEvents {

    @SubscribeEvent
    fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {

        event.registerEntityRenderer(ModEntityTypes.TEDDY_BEAR.get(), ::TeddyBearRenderer)
        event.registerEntityRenderer(ModEntityTypes.STRING_WORM.get(), ::StringWormRenderer)
        event.registerEntityRenderer(ModEntityTypes.AARONBERRY.get(), ::AaronberryRenderer)
        event.registerEntityRenderer(ModEntityTypes.DODGEBALL.get(), ::ThrownItemRenderer)
        event.registerEntityRenderer(ModEntityTypes.SHRINK_RAY_PROJECTILE.get(), ::ShrinkRayProjectileRenderer)
        event.registerEntityRenderer(ModEntityTypes.STICKY_HAND_PROJECTILE.get(), ::StickyHandProjectileRenderer)
        event.registerEntityRenderer(ModEntityTypes.POGO_STICK_VEHICLE.get(), ::PogoStickVehicleRenderer)

        //TODO
//        CuriosRendererRegistry.register(
//            ModItems.SEEING_STONE.get(),
//            ::SeeingStoneCurioRenderer
//        )

    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

        ItemProperties.register(
            ModItems.TOY_GUN.get(),
            ModItemModelProvider.whimsyPredicateName,
            ItemPropertyFunction { stack, _, _, _ ->
                val item = stack.item as? ToyGunItem ?: return@ItemPropertyFunction 0f

                if (ClientUtil.hasWhimsy(item.requiredWhimsy)) 1f else 0f
            }
        )

    }

    @SubscribeEvent
    fun addLayers(event: EntityRenderersEvent.AddLayers) {
        // Grabbed essentially in its entirety from https://github.com/mekanism/Mekanism/blob/0601185d5826b5a195f6148aab4d35761141f806/src/main/java/mekanism/client/ClientRegistration.java#L625

        // Add to player renderers
        for (skin in event.skins) {
            val renderer = event.getSkin(skin) as? PlayerRenderer
            if (renderer != null) {
                addCustomLayers(EntityType.PLAYER, renderer, event.context)
            }
        }

        // Add to everything that has an armor layer
        for (entityType in event.entityTypes) {
            val renderer = event.getRenderer(entityType) as? LivingEntityRenderer<*, *>
            if (renderer != null) {
                addCustomLayers(entityType, renderer, event.context)
            }
        }

    }

    private fun <E : LivingEntity, M : EntityModel<E>> addCustomLayers(
        entityType: EntityType<*>,
        renderer: LivingEntityRenderer<E, M>,
        context: EntityRendererProvider.Context
    ) {

        val layersToAdd: MutableMap<String, RenderLayer<E, M>> = mutableMapOf()

        for (layerRenderer in renderer.layers) {
            if (layerRenderer == null) continue

            val layerClass = layerRenderer.javaClass
            if (layerClass == ElytraLayer::class.java) {
                layersToAdd["TowelCape"] = TowelCapeLayer(renderer, context.modelSet)

                break
            }
        }

        if (layersToAdd.isNotEmpty()) {
            val entityName = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)

            for ((name, renderLayer) in layersToAdd) {
                renderer.addLayer(renderLayer)

                Paracosm.LOGGER.debug("Added $name layer to $entityName")
            }
        }

    }

}