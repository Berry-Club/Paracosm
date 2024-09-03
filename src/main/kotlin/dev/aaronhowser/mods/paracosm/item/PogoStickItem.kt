package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent

class PogoStickItem(
    properties: Properties = Properties()
        .durability(256)
) : Item(properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val usedStack = context.itemInHand
        val level = context.level
        val blockPos = context.clickedPos

        if (!level.isClientSide) {
            val pogoStickVehicle = PogoStickVehicle(
                level = level,
                placeOnBlock = blockPos
            )
            level.addFreshEntity(pogoStickVehicle)
            level.gameEvent(
                GameEvent.ENTITY_PLACE,
                pogoStickVehicle.position(),
                GameEvent.Context.of(context.player)
            )
        }

        usedStack.consume(1, context.player)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

}