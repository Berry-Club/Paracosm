package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.paracosm.datagen.language.ModItemLang
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
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
				.displayItems { a, b ->
					b.acceptAll(ModItems.ITEM_REGISTRY.entries.map { (it as DeferredItem).toStack() })
				}
				.build()
		})

}