package dev.aaronhowser.mods.paracosm.registry

import dev.aaronhowser.mods.aaron.registry.AaronArmorMaterialRegistry
import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ArmorMaterial
import net.neoforged.neoforge.registries.DeferredRegister

object ModArmorMaterials : AaronArmorMaterialRegistry() {

	val ARMOR_MATERIAL_REGISTRY: DeferredRegister<ArmorMaterial> =
		DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Paracosm.MOD_ID)

	override fun getArmorMaterialRegistry(): DeferredRegister<ArmorMaterial> = ARMOR_MATERIAL_REGISTRY

	val LIGHT_UP_SHOES =
		Builder("light_up_shoes")
			.armor(0)
			.register()

}