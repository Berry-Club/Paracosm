package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.entity.custom.ShrinkRayProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.ArrowRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class ShrinkRayProjectileRenderer(
	context: EntityRendererProvider.Context
) : ArrowRenderer<ShrinkRayProjectile>(context) {

	companion object {
		val SHRINK_TEXTURE = OtherUtil.modResource("textures/entity/projectile/shrink_ray.png")
		val GROW_TEXTURE = OtherUtil.modResource("textures/entity/projectile/shrink_ray_grow.png")
	}

	override fun getTextureLocation(projectile: ShrinkRayProjectile): ResourceLocation {
		return if (projectile.isGrow) {
			GROW_TEXTURE
		} else {
			SHRINK_TEXTURE
		}
	}

}