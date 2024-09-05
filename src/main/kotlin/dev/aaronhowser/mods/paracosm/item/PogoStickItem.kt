package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.entity.custom.PogoStickVehicle
import dev.aaronhowser.mods.paracosm.item.component.StringListComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModEntityTypes
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3

class PogoStickItem(
    properties: Properties = Properties()
        .durability(256)
        .component(ModDataComponents.UPGRADES, StringListComponent())
) : Item(properties) {

    object Upgrades {
        const val DOUBLE_JUMP = "double_jump"
        const val LOWER_GRAVITY = "lower_gravity"
        const val GOOMBA_STOMP = "goomba_stomp"
    }

    companion object {
        private fun getPlacementPos(context: UseOnContext): Vec3 {
            val level = context.level
            val blockPos = context.clickedPos
            val sideClicked = context.clickedFace

            if (sideClicked == Direction.UP) {
                val blockState = level.getBlockState(blockPos)
                val blockHeight = blockState.getShape(level, blockPos).max(Direction.Axis.Y)

                return Vec3(
                    blockPos.x + 0.5,
                    blockPos.y + blockHeight,
                    blockPos.z + 0.5
                )
            }

            if (sideClicked == Direction.DOWN) {
                return Vec3(
                    blockPos.x + 0.5,
                    blockPos.y.toDouble() - ModEntityTypes.POGO_STICK_VEHICLE.get().height,
                    blockPos.z + 0.5
                )
            }

            return Vec3(
                blockPos.x + 0.5 + sideClicked.stepX,
                blockPos.y.toDouble() + sideClicked.stepY,
                blockPos.z + 0.5 + sideClicked.stepZ
            )
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val usedStack = context.itemInHand
        val level = context.level

        if (!level.isClientSide) {
            val pogoStickVehicle = PogoStickVehicle(
                level = level,
                spawnLocation = getPlacementPos(context)
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