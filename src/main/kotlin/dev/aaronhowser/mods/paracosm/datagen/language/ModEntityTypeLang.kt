package dev.aaronhowser.mods.paracosm.datagen.language

import dev.aaronhowser.mods.paracosm.datagen.ModLanguageProvider
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes

object ModEntityTypeLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addEntityType(ModEntityTypes.TEDDY_BEAR, "Teddy Bear")
			addEntityType(ModEntityTypes.DODGEBALL, "Dodgeball")
			addEntityType(ModEntityTypes.STRING_WORM, "String Worm")
			addEntityType(ModEntityTypes.AARONBERRY, "Aaronberry")
			addEntityType(ModEntityTypes.TOY_SOLDIER_GUNNER, "Toy Soldier (Gunner)")
			addEntityType(ModEntityTypes.TOY_SOLDIER_GRENADIER, "Toy Soldier (Grenadier)")
			addEntityType(ModEntityTypes.FOAM_DART, "Foam Dart")
			addEntityType(ModEntityTypes.SHRINK_RAY_PROJECTILE, "Shrink Ray Projectile")
			addEntityType(ModEntityTypes.STICKY_HAND_PROJECTILE, "Sticky Hand Projectile")
			addEntityType(ModEntityTypes.POGO_STICK_VEHICLE, "Pogo Stick")
		}
	}

}