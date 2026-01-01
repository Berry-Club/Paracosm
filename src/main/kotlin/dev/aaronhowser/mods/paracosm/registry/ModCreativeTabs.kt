package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.AaronExtensions.withComponent
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.paracosm.datagen.language.ModItemLang
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeTabs {

	val CREATIVE_TAB_REGISTRY: DeferredRegister<CreativeModeTab> =
		DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Paracosm.MOD_ID)

	val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> =
		CREATIVE_TAB_REGISTRY.register("pitch_perfect", Supplier {
			CreativeModeTab.builder()
				.title(ModItemLang.CREATIVE_TAB.toComponent())
				.icon { ModItems.COTTON.toStack() }
				.displayItems { displayContext, output ->
					val itemsToSkip = listOf(
						ModItems.TOY_SOLDIER_BUCKET.get()
					).toSet()

					val allItems = ModItems.ITEM_REGISTRY.entries.map(DeferredHolder<Item, out Item>::get)

					output.acceptAll((allItems - itemsToSkip).map(Item::getDefaultInstance))

					output.accept(
						ModItems.TOY_SOLDIER_BUCKET
							.withComponent(
								DataComponents.CONTAINER,
								ItemContainerContents.fromItems(
									listOf(
										ModItems.TOY_SOLDIER.get().defaultInstance,
										ModItems.TOY_SOLDIER.get().defaultInstance,
										ModItems.TOY_SOLDIER.get().defaultInstance,
										ModItems.TOY_SOLDIER.get().defaultInstance,
									)
								)
							)
					)

				}
				.build()
		})

}