package dev.aaronhowser.mods.paracosm.client.render.entity

import dev.aaronhowser.mods.paracosm.entity.custom.ShrinkRayProjectile
import net.minecraft.client.renderer.entity.ArrowRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class ShrinkRayProjectileRenderer(
    context: EntityRendererProvider.Context
) : ArrowRenderer<ShrinkRayProjectile>(context) {

    override fun getTextureLocation(p0: ShrinkRayProjectile): ResourceLocation {
        return ResourceLocation.withDefaultNamespace("textures/entity/projectiles/spectral_arrow.png")
    }

}