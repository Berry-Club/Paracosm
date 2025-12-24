package dev.aaronhowser.mods.paracosm.item

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

		return OtherUtil.modResource("textures/models/armor/mask/$face.png")
	}

}