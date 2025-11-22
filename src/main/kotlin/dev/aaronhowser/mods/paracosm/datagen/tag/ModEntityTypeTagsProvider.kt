package dev.aaronhowser.mods.paracosm.datagen.tag

import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.tags.EntityTypeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
	pOutput: PackOutput,
	pProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, Paracosm.MOD_ID, existingFileHelper) {

	override fun addTags(pProvider: HolderLookup.Provider) {

		tag(EntityTypeTags.REDIRECTABLE_PROJECTILE)
			.add(ModEntityTypes.DODGEBALL.get())

		tag(TOYS)
			.add(
				ModEntityTypes.AARONBERRY.get(),
				ModEntityTypes.TEDDY_BEAR.get(),
				ModEntityTypes.STRING_WORM.get(),
				ModEntityTypes.DODGEBALL.get()
			)

		tag(AFFECTED_BY_ZOMBIE_MASK)
			.addTag(EntityTypeTags.ZOMBIES)

		tag(AFFECTED_BY_SKELETON_MASK)
			.addTag(EntityTypeTags.SKELETONS)

		tag(AFFECTED_BY_CREEPER_MASK)
			.add(EntityType.CREEPER)

	}

	companion object {
		private fun create(name: String): TagKey<EntityType<*>> {
			val rl = OtherUtil.modResource(name)
			return TagKey.create(Registries.ENTITY_TYPE, rl)
		}

		val TOYS = create("toys")

		val AFFECTED_BY_ZOMBIE_MASK = create("affected_by_zombie_mask")
		val AFFECTED_BY_SKELETON_MASK = create("affected_by_skeleton_mask")
		val AFFECTED_BY_CREEPER_MASK = create("affected_by_creeper_mask")
	}

}