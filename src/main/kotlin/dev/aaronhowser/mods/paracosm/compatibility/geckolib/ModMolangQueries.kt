package dev.aaronhowser.mods.paracosm.compatibility.geckolib

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import software.bernie.geckolib.loading.math.MolangQueries

object ModMolangQueries {

    fun registerQueries() {
        MolangQueries.setActorVariable<PogoStickVehicle>("query.paracosm_tilt_forward") {
            it.animatable.entityData.get(PogoStickVehicle.DATA_TILT_FORWARD).toDouble()
        }

        MolangQueries.setActorVariable<PogoStickVehicle>("query.paracosm_tilt_left") {
            it.animatable.entityData.get(PogoStickVehicle.DATA_TILT_LEFT).toDouble()
        }

        Paracosm.LOGGER.info("Registered Molang queries")
    }

}