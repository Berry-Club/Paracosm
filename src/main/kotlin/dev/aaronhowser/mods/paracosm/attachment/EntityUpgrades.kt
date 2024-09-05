package dev.aaronhowser.mods.paracosm.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.world.entity.Entity

data class EntityUpgrades(
    val upgrades: Set<String>
) {

    constructor() : this(emptySet())
    constructor(list: List<String>) : this(list.toSet())

    companion object {
        val CODEC: Codec<EntityUpgrades> =
            Codec.STRING.listOf().xmap(::EntityUpgrades) { it.upgrades.toList() }

        var Entity.upgrades: Set<String>
            get() = this.getData(ModAttachmentTypes.ENTITY_UPGRADES).upgrades
            set(value) {
                this.setData(ModAttachmentTypes.ENTITY_UPGRADES, EntityUpgrades(value))
            }

        fun Entity.addUpgrade(upgrade: String) {
            upgrades = upgrades + upgrade
        }

        fun Entity.removeUpgrade(upgrade: String) {
            upgrades = upgrades - upgrade
        }

        fun Entity.hasUpgrade(upgrade: String): Boolean {
            return upgrade in upgrades
        }
    }

}