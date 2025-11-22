package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.paracosm.item.*
import dev.aaronhowser.mods.paracosm.item.base.FancyFoodItem
import dev.aaronhowser.mods.paracosm.item.base.FancyFoodItem.Companion.fast
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
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

	val ZOMBIE_MASK: DeferredItem<ArmorItem> =
		halloweenMask("zombie_mask", ModEntityTypeTagsProvider.AFFECTED_BY_ZOMBIE_MASK)
	val SKELETON_MASK: DeferredItem<ArmorItem> =
		halloweenMask("skeleton_mask", ModEntityTypeTagsProvider.AFFECTED_BY_SKELETON_MASK)
	val CREEPER_MASK: DeferredItem<ArmorItem> =
		halloweenMask("creeper_mask", ModEntityTypeTagsProvider.AFFECTED_BY_CREEPER_MASK)

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
					.fast(0.1f)
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

	private fun halloweenMask(
		id: String,
		neutralEntities: TagKey<EntityType<*>>
	): DeferredItem<ArmorItem> {
		return ITEM_REGISTRY.registerItem(id) {
			ArmorItem(
				ModArmorMaterials.HALLOWEEN_MASK,
				ArmorItem.Type.HELMET,
				Item.Properties()
					.stacksTo(1)
					.component(ModDataComponents.AGGRO_IMMUNE_FROM, neutralEntities)
			)
		}
	}

}