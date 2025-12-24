package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.paracosm.registry.ModArmorMaterials
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ItemStack

class HalloweenMaskItem(properties: Properties) : ArmorItem(ModArmorMaterials.HALLOWEEN_MASK, Type.HELMET, properties) {

	override fun getArmorTexture(
		stack: ItemStack,
		entity: Entity,
		slot: EquipmentSlot,
		layer: ArmorMaterial.Layer,
		innerModel: Boolean
	): ResourceLocation? {
		val face = stack.get(ModDataComponents.MASK_FACE)
			?: return super.getArmorTexture(stack, entity, slot, layer, innerModel)

		return OtherUtil.modResource("textures/models/armor/halloween_mask/$face.png")
	}

	companion object {
		val DEFAULT_CREEPER_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.MASK_FACE, "creeper")
				.component(ModDataComponents.AGGRO_IMMUNE_FROM, ModEntityTypeTagsProvider.AFFECTED_BY_CREEPER_MASK)
		}

		val DEFAULT_SKELETON_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.MASK_FACE, "skeleton")
				.component(ModDataComponents.AGGRO_IMMUNE_FROM, ModEntityTypeTagsProvider.AFFECTED_BY_SKELETON_MASK)
		}

		val DEFAULT_ZOMBIE_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.MASK_FACE, "zombie")
				.component(ModDataComponents.AGGRO_IMMUNE_FROM, ModEntityTypeTagsProvider.AFFECTED_BY_ZOMBIE_MASK)
		}
	}

}