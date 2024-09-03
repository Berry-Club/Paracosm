package dev.aaronhowser.mods.paracosm.compatibility.geckolib

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import software.bernie.geckolib.loading.math.MolangQueries

object ParacosmQueries {

    const val POGO_COMPRESSED = "query.pogo_compressed"

    fun setQueryValues() {
        MolangQueries.setActorVariable<PogoStickVehicle>(POGO_COMPRESSED) { actor ->
            actor.animatable.entityData.get(PogoStickVehicle.jumpAmount).toDouble()
        }
    }

}