package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.util.Upgradeable
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.Entity

interface IUpgradeableEntity {

	companion object {
		private const val UPGRADES_TAG = "${Paracosm.MOD_ID}.upgrades"
	}

	fun saveUpgrades(entity: Entity, compoundTag: CompoundTag) {
		val upgradesList = compoundTag.getList(UPGRADES_TAG, Tag.TAG_STRING.toInt())
		for (upgrade in Upgradeable.getUpgrades(entity)) {
			upgradesList.add(StringTag.valueOf(upgrade))
		}
	}

	fun loadUpgrades(entity: Entity, compoundTag: CompoundTag) {
		val upgradesList = compoundTag.getList(UPGRADES_TAG, Tag.TAG_STRING.toInt())
		for (tag in upgradesList) {
			tag as? StringTag ?: continue
			Upgradeable.addUpgrade(entity, tag.asString)
		}
	}

}