package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.research.ModResearchTypes
import dev.aaronhowser.mods.paracosm.research.ResearchType
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.CompletableFuture

class ModMcFunctionProvider(
	private val packOutput: PackOutput
) : DataProvider {

	override fun run(output: CachedOutput): CompletableFuture<*> {
		TODO("Not yet implemented")
	}

	override fun getName(): String = "Paracosm McFunction Provider"

	companion object {
		private fun giveOnePoint(researchType: ResourceKey<ResearchType>): ResourceLocation {
			return giveOnePoint(researchType.location().path)
		}

		private fun giveOnePoint(path: String): ResourceLocation {
			return OtherUtil.modResource("research/give_one_point_${path}")
		}

		val GIVE_ONE_ADVENTURE = giveOnePoint(ModResearchTypes.ADVENTURE)
		val GIVE_ONE_MISCHIEF = giveOnePoint(ModResearchTypes.MISCHIEF)
		val GIVE_ONE_COMFORT = giveOnePoint(ModResearchTypes.COMFORT)
		val GIVE_ONE_ACTION = giveOnePoint(ModResearchTypes.ACTION)
		val GIVE_ONE_MOVEMENT = giveOnePoint(ModResearchTypes.MOVEMENT)
		val GIVE_ONE_FRIENDSHIP = giveOnePoint(ModResearchTypes.FRIENDSHIP)

		val GIVE_ONE_CONTRAPTIONS = giveOnePoint(ModResearchTypes.CONTRAPTIONS)
		val GIVE_ONE_SPOOKY = giveOnePoint(ModResearchTypes.SPOOKY)
	}

}