package dev.aaronhowser.mods.paracosm.research

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.jvm.optionals.getOrNull

object ModResearchTypes {

	val RESEARCH_TYPE_RK: ResourceKey<Registry<ResearchType>> =
		ResourceKey.createRegistryKey(Paracosm.modResource("research_type"))

	fun getGeneRegistry(registries: HolderLookup.Provider): HolderLookup.RegistryLookup<ResearchType> {
		return registries.lookupOrThrow(RESEARCH_TYPE_RK)
	}

	fun fromResourceLocation(
		registries: HolderLookup.Provider,
		rl: ResourceLocation
	): Holder.Reference<ResearchType>? {
		val rk = ResourceKey.create(RESEARCH_TYPE_RK, rl)
		return getGeneRegistry(registries).get(rk).getOrNull()
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
		return ResourceKey.create(RESEARCH_TYPE_RK, Paracosm.modResource(name))
	}

	val COMMAND_SUGGESTION = SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
		val allLocations = getGeneRegistry(context.source.registryAccess())
			.listElementIds()
			.map { it.location().toString() }

		SharedSuggestionProvider.suggest(allLocations, suggestionsBuilder)
	}

}