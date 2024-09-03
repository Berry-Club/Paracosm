package dev.aaronhowser.mods.paracosm

import dev.aaronhowser.mods.paracosm.compatibility.geckolib.ParacosmQueries
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(
    value = Paracosm.ID,
    dist = [Dist.CLIENT]
)
class ParacosmClient(modContainer: ModContainer) {

    init {
        Paracosm.LOGGER.info("Hello, Paracosm client!")

        ParacosmQueries.setQueryValues()
    }

}