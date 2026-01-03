package dev.aaronhowser.mods.paracosm.client.render.armor

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.item.PropellerHatItem
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer

class PropellerHatRenderer : GeoArmorRenderer<PropellerHatItem>(DefaultedItemGeoModel(Paracosm.modResource("armor/propeller_hat")))