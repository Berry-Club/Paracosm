package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec

data class EntityUpgrades(
	val upgrades: Set<String>
) {

	constructor() : this(emptySet())
	constructor(list: List<String>) : this(list.toSet())

	companion object {
		val CODEC: Codec<EntityUpgrades> =
			Codec.STRING.listOf().xmap(::EntityUpgrades) { it.upgrades.toList() }
	}

}