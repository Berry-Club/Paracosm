package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(output: PackOutput) : LanguageProvider(output, Paracosm.ID, "en_us") {

    object Item {
        const val COTTON = "item.paracosm.cotton"
        const val TOWEL_CAPE = "item.paracosm.towel_cape"
        const val SEEING_STONE = "item.paracosm.seeing_stone"
    }

    object Block {
        const val NIGHT_LIGHT = "block.paracosm.night_light"
    }

    object Entity {
        const val TEDDY_BEAR = "entity.paracosm.teddy_bear"
    }

    object Misc {
        const val CREATIVE_TAB = "itemGroup.paracosm"
    }

    object Curios {
        const val SEEING_STONE = "curios.identifier.seeing_stone"
    }

    companion object {
        fun String.toComponent(vararg args: Any): MutableComponent = Component.translatable(this, *args)
    }

    override fun addTranslations() {
        add(Item.COTTON, "Cotton")
        add(Item.TOWEL_CAPE, "Towel Cape")
        add(Item.SEEING_STONE, "Seeing Stone")

        add(Block.NIGHT_LIGHT, "Night Light")

        add(Entity.TEDDY_BEAR, "Teddy Bear")

        add(Misc.CREATIVE_TAB, "Paracosm")

        add(Curios.SEEING_STONE, "Seeing Stone")
    }
}