package dev.aaronhowser.mods.paracosm.item.component

import com.mojang.datafixers.util.Either
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.component.CustomData

data class ToySoldierDataComponent(
	val either: Either<ResourceLocation, CustomData>
) {

	constructor(location: ResourceLocation) : this(Either.left(location))
	constructor(data: CustomData) : this(Either.right(data))

	companion object {
		fun fromEntity(entity: LivingEntity): ToySoldierDataComponent {
			val nbt = CompoundTag()
			entity.save(nbt)

			val customData = CustomData.of(nbt)
			return ToySoldierDataComponent(customData)
		}
	}

}