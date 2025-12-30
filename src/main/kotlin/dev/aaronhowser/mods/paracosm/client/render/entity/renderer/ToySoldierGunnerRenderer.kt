package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.client.render.entity.model.ToySoldierGunnerModel
import dev.aaronhowser.mods.paracosm.entity.ToySoldierGunnerEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class ToySoldierGunnerRenderer(
	renderManager: EntityRendererProvider.Context,
) : GeoEntityRenderer<ToySoldierGunnerEntity>(renderManager, ToySoldierGunnerModel()) {

}