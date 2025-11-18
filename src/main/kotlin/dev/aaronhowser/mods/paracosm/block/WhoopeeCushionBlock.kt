package dev.aaronhowser.mods.paracosm.block

import dev.aaronhowser.mods.paracosm.registry.ModSounds
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

class WhoopeeCushionBlock : PressurePlateBlock(
	blockSet,
	Properties.of()
		.mapColor(MapColor.COLOR_PINK)
		.forceSolidOn()
		.instrument(NoteBlockInstrument.BASS)
		.noCollission()
		.strength(0.5f)
		.ignitedByLava()
		.pushReaction(PushReaction.DESTROY)
) {

	companion object {
		private val blockSet = BlockSetType.register(
			BlockSetType(
				"whoopee_cushion",
				true,
				true,
				true,
				BlockSetType.PressurePlateSensitivity.EVERYTHING,
				SoundType.CHERRY_WOOD,
				SoundEvents.CHERRY_WOOD_DOOR_CLOSE,
				SoundEvents.CHERRY_WOOD_DOOR_OPEN,
				SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE,
				SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN,
				ModSounds.UNFART.get(),
				ModSounds.FART.get(),
				SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF,
				SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON
			)
		)
	}

}