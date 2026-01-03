package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.projectile.ShrinkRayProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.entity.ArrowRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class ShrinkRayProjectileRenderer(
	context: EntityRendererProvider.Context
) : ArrowRenderer<ShrinkRayProjectile>(context) {

	override fun getTextureLocation(projectile: ShrinkRayProjectile): ResourceLocation {
		return if (projectile.isGrow) {
			GROW_TEXTURE
		} else {
			SHRINK_TEXTURE
		}
	}

	companion object {
		val SHRINK_TEXTURE = Paracosm.modResource("textures/entity/shrink_ray/shrink.png")
		val GROW_TEXTURE = Paracosm.modResource("textures/entity/shrink_ray/grow.png")
	}

}