package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.datagen.language.ModSubtitleLang
import dev.aaronhowser.mods.paracosm.registry.ModSounds
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider

class ModSoundDefinitionsProvider(
	output: PackOutput,
	helper: ExistingFileHelper
) : SoundDefinitionsProvider(output, Paracosm.MOD_ID, helper) {

	override fun registerSounds() {
		add(
			ModSounds.FART,
			SoundDefinition.definition()
				.with(
					sound(Paracosm.modResource("fart1"), SoundDefinition.SoundType.SOUND),
					sound(Paracosm.modResource("fart2"), SoundDefinition.SoundType.SOUND),
					sound(Paracosm.modResource("fart3"), SoundDefinition.SoundType.SOUND),
				)
				.subtitle(ModSubtitleLang.FART)
		)

		add(
			ModSounds.UNFART,
			SoundDefinition.definition()
				.with(
					sound(Paracosm.modResource("unfart1"), SoundDefinition.SoundType.SOUND),
					sound(Paracosm.modResource("unfart2"), SoundDefinition.SoundType.SOUND),
					sound(Paracosm.modResource("unfart3"), SoundDefinition.SoundType.SOUND),
				)
				.subtitle(ModSubtitleLang.UNFART)
		)

		add(
			ModSounds.SQUEE,
			SoundDefinition.definition()
				.with(
					sound(Paracosm.modResource("squee"), SoundDefinition.SoundType.SOUND)
				)
				.subtitle(ModSubtitleLang.SQUEE)
		)

		add(
			ModSounds.DODGEBALL,
			SoundDefinition.definition()
				.with(
					sound(Paracosm.modResource("dodgeball"), SoundDefinition.SoundType.SOUND)
				)
				.subtitle(ModSubtitleLang.DODGEBALL)
		)

		//FIXME: You can't use vanilla sounds here
//        add(
//            ModSounds.STICKY_HAND_THROW,
//            SoundDefinition.definition()
//                .with(
//                    sound(SoundEvents.SLIME_BLOCK_PLACE.location)
//                )
//                .subtitle(ModSubtitleLang.STICKY_HAND_THROW)
//        )
//
//        add(
//            ModSounds.STICKY_HAND_RETRIEVE,
//            SoundDefinition.definition()
//                .with(
//                    sound(SoundEvents.SLIME_BLOCK_BREAK.location)
//                )
//                .subtitle(ModSubtitleLang.STICKY_HAND_RETRIEVE)
//        )

	}

}