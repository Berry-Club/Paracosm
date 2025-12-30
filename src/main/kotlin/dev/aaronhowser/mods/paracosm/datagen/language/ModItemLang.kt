package dev.aaronhowser.mods.paracosm.datagen.language

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider
import dev.aaronhowser.mods.paracosm.registry.ModItems

object ModItemLang {

	const val CREATIVE_TAB = "itemGroup.paracosm"

	const val TOY_GUN = "item.paracosm.toy_gun"
	const val COOL_GUN = "item.paracosm.cool_gun"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(CREATIVE_TAB, "Paracosm")

			addItem(ModItems.COTTON, "Cotton")
			addItem(ModItems.TOWEL_CAPE, "Towel Cape")
			addItem(ModItems.SEEING_STONE, "Seeing Stone")
			addItem(ModItems.DODGEBALL, "Dodgeball")
			addItem(ModItems.CANDY, "Candy")
			addItem(ModItems.SODA, "Soda")
			addItem(ModItems.WARM_MILK, "Warm Milk")
			addItem(ModItems.SHRINK_RAY, "Shrink Ray")
			addItem(ModItems.STICKY_HAND, "Sticky Hand")
			addItem(ModItems.POGO_STICK, "Pogo Stick")
			addItem(ModItems.DUCK_HUNT_GUN, "Duck Hunt Gun")
			addItem(ModItems.TOY_SOLDIER, "Toy Soldier")

			addItem(ModItems.NERF_GUN, "Nerf Gun")
			addItem(ModItems.FOAM_DART, "Foam Dart")

			addItem(ModItems.ZOMBIE_MASK, "Zombie Mask")
			addItem(ModItems.SKELETON_MASK, "Skeleton Mask")
			addItem(ModItems.CREEPER_MASK, "Creeper Mask")
			addItem(ModItems.PROPELLER_HAT, "Propeller Hat")
			addItem(ModItems.LIGHT_UP_SHOES, "Light-Up Shoes")

			addItem(ModItems.HULA_HOOP, "Hula Hoop")

			add(TOY_GUN, "Toy Gun")
			add(COOL_GUN, "Cool Gun")
		}
	}

}