package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.block.*
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(Paracosm.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	val COTTON: DeferredBlock<CottonBlock> =
		registerBlockWithoutItem("cotton", ::CottonBlock)
	val NIGHT_LIGHT: DeferredBlock<NightLightBlock> =
		registerBlock("night_light", ::NightLightBlock)
	val WHOOPEE_CUSHION: DeferredBlock<WhoopeeCushionBlock> =
		registerBlock("whoopee_cushion", ::WhoopeeCushionBlock)
	val WALRUS: DeferredBlock<WalrusBlock> =
		registerBlock("walrus", ::WalrusBlock)
	val IMAGINATOR: DeferredBlock<ImaginatorBlock> =
		registerBlock("imaginator", ::ImaginatorBlock)
	val CITY_RUG: DeferredBlock<CityRugBlock> =
		registerBlock("city_rug", ::CityRugBlock)

}