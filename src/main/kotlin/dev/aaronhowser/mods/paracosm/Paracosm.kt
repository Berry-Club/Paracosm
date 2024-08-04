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
     * - Multiple levels of imagination, at high levels people with low levels see the effects too, normally they dont. Like, you're warping reality with how much imagination you have
     *
     * Sources
     * - TV with cartoons, says in chat like "You spend some time watching cartoons, you feel more imaginative"
     * - Tree houses
     * - Toys
     * - Coloring books? Could build up imagination as you color. Maybe its imagination storage?
     * - Exploring?
     * - Taming pets
     * - Eating sweets, sweets also have other effects
     * - Video games
     * - Listening to music (via note blocks, jukeboxes, or Pitch Perfect)
     * - Pranks (whoopie cushions, bucket on door)
     *
     * Uses
     * - Toys mob companions that do things but if someone without imagination looks at them, they flop to the ground Toy Story style
     * - A gun that if you have imagination works normal but to everyone else it sounds like me going pew pew
     * - Chalk-based stuff, like hopscotch that makes more agile, portals, portables holes
     * - Mental dimension, mindspace etc? Storage? Mind Palace? What if it gets bigger the more imaginative you are?
     * - If you go to sleep, add a button to go to a dream world
     * - Halloween costumes that actually function
     *
     * Toys
     * - Teddy Bear (farm)
     * - Robot (mine/combat)
     * - Doll (heal)
     * - RC helicopter
     * - String worm steed
     * - EC2 Walrus steed
     * - Amogus
     * - Toy car
     * - Other pop culture references
     * -
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
     * - If you watch the TV at night, you can see bad things (multiple levels of bad, like boring infomercials vs evil stuff)
     * - Listening to the bad music disks (11, 13, etc) could give you bad imagination
     * - Eating too many sweets could give you bad imagination
     * - Maybe a bad imagination mob that spawns in the dark
     * - Punish the player for swearing in chat
     *
     * Misc
     * - Dreamcatchers that do stuff, maybe prevents nightmares and lets you use those dreams for something
     * - Tie in to witch stuff?
     * - Reference Wizard of Oz
     * - Reference a lot of things
     * - Sleepy status effect when you go too long, maybe it really ramps up your bad imagination
     * - Whoopie cushion with imagination can be used to knock mobs away, also it works as a pressure plate but farts
     *
     */

}