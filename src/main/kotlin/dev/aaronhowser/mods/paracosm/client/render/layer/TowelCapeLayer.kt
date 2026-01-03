package dev.aaronhowser.mods.paracosm.client.render.layer

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.ElytraLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class TowelCapeLayer<E : LivingEntity, M : EntityModel<E>>(
	renderer: RenderLayerParent<E, M>,
	modelSet: EntityModelSet
) : ElytraLayer<E, M>(renderer, modelSet) {

	override fun shouldRender(stack: ItemStack, entity: E): Boolean {
		return stack.item == ModItems.TOWEL_CAPE.get()
	}

	override fun getElytraTexture(stack: ItemStack, entity: E): ResourceLocation = TEXTURE

	companion object {
		private val TEXTURE = Paracosm.modResource("textures/entity/towel_cape.png")
	}

}