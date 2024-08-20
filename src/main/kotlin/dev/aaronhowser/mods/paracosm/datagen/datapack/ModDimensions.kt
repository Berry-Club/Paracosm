package dev.aaronhowser.mods.paracosm.datagen.datapack

import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.Level
import net.minecraft.world.level.dimension.DimensionType
import java.util.*

object ModDimensions {

    val DREAM_DIMENSION_ID: ResourceLocation =
        OtherUtil.modResource("dream")
    val DREAM_DIMENSION_TYPE: ResourceKey<DimensionType> =
        ResourceKey.create(Registries.DIMENSION_TYPE, DREAM_DIMENSION_ID)
    val DREAM_DIMENSION_LEVEL: ResourceKey<Level> =
        ResourceKey.create(Registries.DIMENSION, DREAM_DIMENSION_ID)


    fun bootstrap(context: BootstrapContext<DimensionType>) {
        context.register(
            DREAM_DIMENSION_TYPE, DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0,
                true,
                true,
                0,
                256,
                256,
                BlockTags.INFINIBURN_OVERWORLD,
                OtherUtil.modResource("dream"),
                0f,
                DimensionType.MonsterSettings(
                    false,
                    false,
                    UniformInt.of(0, 7),
                    0
                )
            )
        )
    }


}