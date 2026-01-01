package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import dev.aaronhowser.mods.paracosm.client.render.entity.model.ToySoldierGrenadierModel
import dev.aaronhowser.mods.paracosm.client.render.entity.model.ToySoldierGunnerModel
import dev.aaronhowser.mods.paracosm.entity.ToySoldierGrenadierEntity
import dev.aaronhowser.mods.paracosm.entity.ToySoldierGunnerEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import software.bernie.geckolib.renderer.GeoEntityRenderer

class ToySoldierGrenadierRenderer(
	renderManager: EntityRendererProvider.Context,
) : GeoEntityRenderer<ToySoldierGrenadierEntity>(renderManager, ToySoldierGrenadierModel()) {

	init {
		withScale(0.2f)
	}

}