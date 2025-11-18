package dev.aaronhowser.mods.paracosm.datagen

import dev.aaronhowser.mods.paracosm.Paracosm
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosDataProvider
import top.theillusivec4.curios.api.type.capability.ICurio
import java.util.concurrent.CompletableFuture

class ModCurioProvider(
	output: PackOutput,
	fileHelper: ExistingFileHelper?,
	registries: CompletableFuture<HolderLookup.Provider>?
) : CuriosDataProvider(Paracosm.ID, output, fileHelper, registries) {

	companion object {
		const val SEEING_STONE_SLOT = "seeing_stone"
		const val PLAYER_RULE = "player"
	}

	override fun generate(registries: HolderLookup.Provider?, fileHelper: ExistingFileHelper?) {
		this.createSlot(SEEING_STONE_SLOT)
			.size(1)
			.dropRule(ICurio.DropRule.DEFAULT)
			.addCosmetic(true)

		this.createEntities(PLAYER_RULE)
			.addPlayer()
			.addSlots(SEEING_STONE_SLOT)
	}

}