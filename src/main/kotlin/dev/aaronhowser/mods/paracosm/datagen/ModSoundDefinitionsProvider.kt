package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModSounds
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinition
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider

class ModSoundDefinitionsProvider(
    output: PackOutput,
    helper: ExistingFileHelper
) : SoundDefinitionsProvider(output, Paracosm.ID, helper) {

    override fun registerSounds() {

        add(
            ModSounds.FART,
            SoundDefinition.definition()
                .with(
                    sound(OtherUtil.modResource("fart1"), SoundDefinition.SoundType.SOUND),
                    sound(OtherUtil.modResource("fart2"), SoundDefinition.SoundType.SOUND),
                    sound(OtherUtil.modResource("fart3"), SoundDefinition.SoundType.SOUND),
                )
                .subtitle(ModLanguageProvider.Subtitle.FART)
        )

        add(
            ModSounds.UNFART,
            SoundDefinition.definition()
                .with(
                    sound(OtherUtil.modResource("unfart1"), SoundDefinition.SoundType.SOUND),
                    sound(OtherUtil.modResource("unfart2"), SoundDefinition.SoundType.SOUND),
                    sound(OtherUtil.modResource("unfart3"), SoundDefinition.SoundType.SOUND),
                )
                .subtitle(ModLanguageProvider.Subtitle.UNFART)
        )

        add(
            ModSounds.SQUEE,
            SoundDefinition.definition()
                .with(
                    sound(OtherUtil.modResource("squee"), SoundDefinition.SoundType.SOUND)
                )
                .subtitle(ModLanguageProvider.Subtitle.SQUEE)
        )

        add(
            ModSounds.DODGEBALL,
            SoundDefinition.definition()
                .with(
                    sound(OtherUtil.modResource("dodgeball"), SoundDefinition.SoundType.SOUND)
                )
                .subtitle(ModLanguageProvider.Subtitle.DODGEBALL)
        )

        //FIXME: You can't use vanilla sounds here
//        add(
//            ModSounds.STICKY_HAND_THROW,
//            SoundDefinition.definition()
//                .with(
//                    sound(SoundEvents.SLIME_BLOCK_PLACE.location)
//                )
//                .subtitle(ModLanguageProvider.Subtitle.STICKY_HAND_THROW)
//        )
//
//        add(
//            ModSounds.STICKY_HAND_RETRIEVE,
//            SoundDefinition.definition()
//                .with(
//                    sound(SoundEvents.SLIME_BLOCK_BREAK.location)
//                )
//                .subtitle(ModLanguageProvider.Subtitle.STICKY_HAND_RETRIEVE)
//        )

    }

}