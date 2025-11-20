package dev.aaronhowser.mods.paracosm.research

import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object ModResearchTypes {

	val RESEARCH_TYPE_RK: ResourceKey<Registry<ResearchType>> =
		ResourceKey.createRegistryKey(OtherUtil.modResource("research_type"))

	fun getGeneRegistry(registries: HolderLookup.Provider): HolderLookup.RegistryLookup<ResearchType> {
		return registries.lookupOrThrow(RESEARCH_TYPE_RK)
	}

	val ADVENTURE = rk("adventure")
	val MISCHIEF = rk("mischief")
	val COMFORT = rk("comfort")
	val ACTION = rk("action")
	val MOVEMENT = rk("movement")
	val FRIENDSHIP = rk("friendship")

	val CONTRAPTIONS = rk("contraptions")
	val SPOOKY = rk("spooky")

	private fun rk(name: String): ResourceKey<ResearchType> {
		return ResourceKey.create(RESEARCH_TYPE_RK, OtherUtil.modResource(name))
	}

}