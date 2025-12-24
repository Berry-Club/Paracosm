package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.*
import dev.aaronhowser.mods.paracosm.item.base.FancyFoodItem
import dev.aaronhowser.mods.paracosm.item.base.FancyFoodItem.Companion.eatTime
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemNameBlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems : AaronItemRegistry() {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Paracosm.MOD_ID)
	override fun getItemRegistry(): DeferredRegister.Items = ITEM_REGISTRY

	val COTTON: DeferredItem<ItemNameBlockItem> =
		registerItemNameBlockItem("cotton", ModBlocks.COTTON)
	val TOY_GUN: DeferredItem<ToyGunItem> =
		register("toy_gun", ::ToyGunItem, ToyGunItem.DEFAULT_PROPERTIES)
	val TOWEL_CAPE: DeferredItem<TowelCapeItem> =
		register("towel_cape", ::TowelCapeItem, TowelCapeItem.DEFAULT_PROPERTIES)
	val SEEING_STONE: DeferredItem<SeeingStone> =
		register("seeing_stone", ::SeeingStone, SeeingStone.DEFAULT_PROPERTIES)
	val DODGEBALL: DeferredItem<DodgeballItem> =
		register("dodgeball", ::DodgeballItem, DodgeballItem.DEFAULT_PROPERTIES)
	val SHRINK_RAY: DeferredItem<ShrinkRayItem> =
		register("shrink_ray", ::ShrinkRayItem, ShrinkRayItem.DEFAULT_PROPERTIES)
	val STICKY_HAND: DeferredItem<StickyHandItem> =
		register("sticky_hand", ::StickyHandItem, StickyHandItem.DEFAULT_PROPERTIES)
	val POGO_STICK: DeferredItem<PogoStickItem> =
		register("pogo_stick", ::PogoStickItem, PogoStickItem.DEFAULT_PROPERTIES)
	val DUCK_HUNT_GUN: DeferredItem<DuckHuntGunItem> =
		register("duck_hunt_gun", ::DuckHuntGunItem, DuckHuntGunItem.DEFAULT_PROPERTIES)
	val ZOMBIE_MASK: DeferredItem<HalloweenMaskItem> =
		register("zombie_mask", ::HalloweenMaskItem, HalloweenMaskItem.DEFAULT_ZOMBIE_PROPERTIES)
	val SKELETON_MASK: DeferredItem<HalloweenMaskItem> =
		register("skeleton_mask", ::HalloweenMaskItem, HalloweenMaskItem.DEFAULT_SKELETON_PROPERTIES)
	val CREEPER_MASK: DeferredItem<HalloweenMaskItem> =
		register("creeper_mask", ::HalloweenMaskItem, HalloweenMaskItem.DEFAULT_CREEPER_PROPERTIES)
	val NERF_GUN: DeferredItem<NerfGunItem> =
		register("nerf_gun", ::NerfGunItem, NerfGunItem.DEFAULT_PROPERTIES)
	val FOAM_DART: DeferredItem<FoamDartItem> =
		register("foam_dart", ::FoamDartItem, FoamDartItem.DEFAULT_PROPERTIES)
	val PROPELLER_HAT: DeferredItem<PropellerHatItem> =
		register("propeller_hat", ::PropellerHatItem, PropellerHatItem.DEFAULT_PROPERTIES)
	val HULA_HOOP: DeferredItem<HulaHoopItem> =
		register("hula_hoop", ::HulaHoopItem, HulaHoopItem.DEFAULT_PROPERTIES)
	val LIGHT_UP_SHOES: DeferredItem<LightUpShoesItem> =
		register("light_up_shoes", ::LightUpShoesItem, LightUpShoesItem.DEFAULT_PROPERTIES)

	// Foods
	val CANDY: DeferredItem<FancyFoodItem> =
		ITEM_REGISTRY.registerItem("candy") {
			FancyFoodItem(
				stacksTo = 99,
				isDrink = false,
				FoodProperties
					.Builder()
					.nutrition(1)
					.saturationModifier(-0.1f)
					.eatTime(0.1f)
					.build()
			)
		}
	val SODA: DeferredItem<FancyFoodItem> =
		ITEM_REGISTRY.registerItem("soda") {
			FancyFoodItem(
				stacksTo = 1,
				isDrink = true,
				FoodProperties
					.Builder()
					.nutrition(1)
					.saturationModifier(0.1f)
					.build()
			)
		}
	val WARM_MILK: DeferredItem<WarmMilkItem> =
		register("warm_milk", ::WarmMilkItem, WarmMilkItem.DEFAULT_PROPERTIES)

}