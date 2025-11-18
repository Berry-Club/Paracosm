package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.EntityTypeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
	pOutput: PackOutput,
	pProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, Paracosm.ID, existingFileHelper) {

	override fun addTags(pProvider: HolderLookup.Provider) {
		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE)
			.add(ModEntityTypes.DODGEBALL.get())

		this.tag(TOYS)
			.add(
				ModEntityTypes.AARONBERRY.get(),
				ModEntityTypes.TEDDY_BEAR.get(),
				ModEntityTypes.STRING_WORM.get(),
				ModEntityTypes.DODGEBALL.get()
			)
	}

	companion object {
		private fun create(name: String): TagKey<EntityType<*>> {
			val rl = OtherUtil.modResource(name)
			return TagKey.create(Registries.ENTITY_TYPE, rl)
		}

		val TOYS = create("toys")
	}

}