package dev.aaronhowser.mods.paracosm.compatibility.geckolib

import dev.aaronhowser.mods.paracosm.Paracosm
import software.bernie.geckolib.loading.math.MathParser
import software.bernie.geckolib.loading.math.value.Variable

object ModMolangQueries {

    const val TILT_FORWARD = "query.paracosm_tilt_forward"
    const val TILT_LEFT = "query.paracosm_tilt_left"

    fun registerQueries() {

        MathParser.registerVariable(Variable(TILT_FORWARD, 0.0))
        MathParser.registerVariable(Variable(TILT_LEFT, 0.0))

        Paracosm.LOGGER.info("Registered Molang queries")
    }

}