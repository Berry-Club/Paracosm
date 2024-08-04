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
     * - Mod is based on childlike imagination, everything should be kid-themed
     * - An imagination value (Whimsy? Wonder?) that increases when you do certain things and unlocks imagination-based magic
     * - Imagination-based things dont work/exist in the presence of people with low imagination. This even can debuff players with mid imagination if someone with low imagination is watching them, bc that's "proving" that it's not real
     * - At very very high imagination, it becomes real for everyone, like you're warping reality
     * - Textures and sounds are per-player, based on their imagination. So someone with low imagination will see a cardboard tube, someone with high imagination will see a lightsaber, or whatever
     * - Should things be magic by default? Maybe you have to use like a magic fairy wand on things to infuse them with imagination
     * - Maybe a BAD form of imagination, prevented by stuff like night light
     * - Maybe something like the witchery altar, but replace candles and heads with toys etc
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
     * - Sleepovers
     * - Camping stuff (marshmallows, campfires, maybe telling stories?)
     * - Seeing other people use imagination
     *
     * Uses
     * - Toys mob companions that do things but if someone without imagination looks at them, they flop to the ground Toy Story style
     * - A gun that if you have imagination works normal but to everyone else it sounds like me going pew pew
     * - Chalk-based stuff, like hopscotch that makes more agile, portals, portables holes
     * - Mental dimension, mindspace etc? Storage? Mind Palace? What if it gets bigger the more imaginative you are?
     * - If you go to sleep, add a button to go to a dream world
     * - Halloween costumes that actually function
     * - Spongebob imagination box? What would that even be, a dimension?
     * - 3D goggles that, at low imagination just enable stereoscopic, but at high imagination does something else. night vision? show secret doors?
     * - Towel cape. Super hero powers? Maybe a worse elytra?
     * - Cardboard armor that looks different
     * - Minecart pants that make you run fast
     * - Glitter that blinds you (using a new thing rather than the black blindness effect, maybe particles that obscure vision)
     *
     * Toys (some are tools some are mobs)
     * - Teddy Bear (farm)
     * - Robot (mine/combat)
     * - Doll (heal)
     * - RC helicopter
     * - String worm steed
     * - EC2 Walrus steed
     * - Amogus
     * - Toy car
     * - Other pop culture references
     * - Whoopie cushion with imagination can be used to knock mobs away, also it works as a pressure plate but farts
     * - Fidget spinner (FIDGET SPINNER HELICOPTER????)
     * - Stress ball
     * - Dodgeball weapon that knocks back mobs, maybe upgrade it with imagination (homing (rainbow trail?), fire), maybe if you have bad imagination they just fly out of nowhere sometimes and hit you in the face (needs a sound)
     * - Aaronberry plush that looks like https://quickvids.app/v/7397148460467621126
     * - Cardboard tube sword/lightsaber
     * - Flying UFO?
     * - Dragon plush
     * - A hammer that with no imagination looks like a lever that's in the middle with the lever bit perpendicular
     * - Rubber chicken (should it be a mob or a tool?)
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
     * - Eating too many sweets could give you bad imagination or just punishes in general
     * - Maybe a bad imagination mob that spawns in the dark
     * - Punish the player for swearing in chat
     * - Cheese nightmares
     * - Eyes in the dark
     * - Causes more cave noises
     * - Natural cave noises cause bad imagination
     * - Some punishment for being in the dark for too long?
     * - Zombies etc that run towards you super fast then vanish
     * - Stress toys that reduce bad imagination when used or lessen the effects of bad imagination, but cause problems when overused
     *
     * Misc
     * - Dreamcatchers that do stuff, maybe prevents nightmares and lets you use those dreams for something
     * - Tie in to witch stuff?
     * - Reference Wizard of Oz
     * - Reference a lot of things
     * - Sleepy status effect when you go too long, maybe it really ramps up your bad imagination
     * - Use the term Figment for something
     * - Item Tags for foods like #sweets #healthy #junk #fast_food
     * - Computer that works like the TV but kinda different?
     * - Rainbows
     * - Does Quark have an API for its emotes? Spongebob Imagination 🌈
     * - A way to opt out of the visuals of having high imagination, so everything still looks like stupid kid toys rather than hallucinating
     * - Fairy lights
     *
     */

}