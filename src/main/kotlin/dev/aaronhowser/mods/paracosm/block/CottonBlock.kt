package dev.aaronhowser.mods.paracosm.block

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

class CottonBlock(
    properties: Properties = Properties.of()
        .mapColor(MapColor.PLANT)
        .noCollission()
        .randomTicks()
        .instabreak()
        .sound(SoundType.CROP)
        .pushReaction(PushReaction.DESTROY)
) : CropBlock(properties) {

    companion object {
        val CODEC: MapCodec<CottonBlock> = simpleCodec(::CottonBlock)
    }

    override fun codec(): MapCodec<out CropBlock> {
        return CODEC
    }

    override fun getBaseSeedId(): ItemLike {
        return ModItems.COTTON
    }

}