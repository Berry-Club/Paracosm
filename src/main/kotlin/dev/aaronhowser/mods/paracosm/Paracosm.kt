package dev.aaronhowser.mods.paracosm

import dev.aaronhowser.mods.paracosm.registry.ModRegistries
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(Paracosm.ID)
class Paracosm(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "paracosm"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {
        LOGGER.info("Hello, Paracosm!")

        ModRegistries.register(MOD_BUS)
    }

    /**
     * Ideas:
     * - An imagination value (Whimsy? Wonder?) that increases when you do certain things and unlocks imagination-based magic
     * - Should things be magic by default? Maybe you have to use like a magic fairy wand on things to infuse them with imagination
     * - Maybe a BAD form of imagination, prevented by stuff like night light
     *
     * Sources
     * - TV with cartoons
     * - Treehouses
     * - Toys
     * - Coloring books? Could build up imagination as you color. Maybe its imagination storage?
     * - Exploring?
     * - Taming pets
     * - Eating sweets, sweets also have other effects
     *
     * Uses
     * - Toys mob companions that do things but if someone without imagination looks at them, they flop to the ground Toy Story style
     * - A gun that if you have imagination works normal but to everyone else it sounds like me going pew pew
     * - Chalk-based stuff, like hopscotch that makes more agile, portals, portables holes
     * - Mental dimension, mindspace etc? Storage
     * - If you go to sleep, add a button to go to a dream world
     *
     * Spiderwick Chronicles style stuff
     * - Hole with a rock in it, lets anyone see imagination stuff, not required for people with high imagination
     * - Naturally spawning mobs like fairies and stuff
     * - Maybe they live in structure
     *
     * Bad imagination stuff
     * - Seeing people in the distance that aren't there
     * - Nightmares
     * - Herobrine (could have an advancement related to it)
     * - Enemies that other players can't see, but they can actually attack you
     *
     * Misc
     * - Dreamcatchers that do stuff, maybe prevents nightmares and lets you use those dreams for something
     * - Tie in to witch stuff?
     * - Reference Wizard of Oz
     * - Reference a lot of things
     *
     */

}