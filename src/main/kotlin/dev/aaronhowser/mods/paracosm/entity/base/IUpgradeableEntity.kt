package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.attachment.EntityUpgrades
import dev.aaronhowser.mods.paracosm.packet.server_to_client.UpdateEntityUpgrades
import dev.aaronhowser.mods.paracosm.registry.ModAttachmentTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.Entity

interface IUpgradeableEntity {

	companion object {
		private const val UPGRADES_TAG = "${Paracosm.MOD_ID}.upgrades"

		fun getUpgrades(entity: Entity): Set<String> {
			return entity.getData(ModAttachmentTypes.ENTITY_UPGRADES.get()).upgrades
		}

		fun setUpgrades(entity: Entity, upgrades: Set<String>) {
			entity.setData(ModAttachmentTypes.ENTITY_UPGRADES.get(), EntityUpgrades(upgrades))

			if (entity.isServerSide) {
				val packet = UpdateEntityUpgrades(entity.id, upgrades.toList())
				packet.messageAllPlayersTrackingEntity(entity)
			}
		}

		fun addUpgrade(entity: Entity, upgrade: String) {
			setUpgrades(
				entity,
				getUpgrades(entity) + upgrade
			)
		}

		fun removeUpgrade(entity: Entity, upgrade: String) {
			setUpgrades(
				entity,
				getUpgrades(entity) - upgrade
			)
		}

		fun hasUpgrade(entity: Entity, upgrade: String): Boolean {
			return upgrade in getUpgrades(entity)
		}

		fun saveUpgrades(entity: Entity, compoundTag: CompoundTag) {
			val upgradesList = compoundTag.getList(UPGRADES_TAG, Tag.TAG_STRING.toInt())
			for (upgrade in getUpgrades(entity)) {
				upgradesList.add(StringTag.valueOf(upgrade))
			}
		}

		fun loadUpgrades(entity: Entity, compoundTag: CompoundTag) {
			val upgradesList = compoundTag.getList(UPGRADES_TAG, Tag.TAG_STRING.toInt())
			for (tag in upgradesList) {
				tag as? StringTag ?: continue
				addUpgrade(entity, tag.asString)
			}
		}
	}

}