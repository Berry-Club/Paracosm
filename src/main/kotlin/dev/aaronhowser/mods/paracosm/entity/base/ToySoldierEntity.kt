package dev.aaronhowser.mods.paracosm.entity.base

import dev.aaronhowser.mods.aaron.AaronExtensions.giveOrDropStack
import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.withComponent
import dev.aaronhowser.mods.paracosm.entity.goal.ToyLookAtPlayerGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyRandomLookAroundGoal
import dev.aaronhowser.mods.paracosm.entity.goal.ToyStrollGoal
import dev.aaronhowser.mods.paracosm.item.component.ToySoldierDataComponent
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*

abstract class ToySoldierEntity(
	entityType: EntityType<out ToySoldierEntity>,
	level: Level
) : ToyEntity(entityType, level) {

	var squadLeaderUuid: UUID? = null
		private set
	val isSquadLeader: Boolean get() = squadLeaderUuid == null

	override fun registerGoals() {
		goalSelector.addGoal(0, FloatGoal(this))
		goalSelector.addGoal(2, SitWhenOrderedToGoal(this))
		goalSelector.addGoal(3, ToyLookAtPlayerGoal(this))
		goalSelector.addGoal(4, ToyRandomLookAroundGoal(this))
		goalSelector.addGoal(5, FollowOwnerGoal(this, 1.0, 10f, 2f))
		goalSelector.addGoal(6, ToyStrollGoal(this, 1.0))
	}

	override fun getOwner(): LivingEntity? {
		return if (isSquadLeader) {
			super.getOwner()
		} else {
			getSquadLeader()
		}
	}

	fun getSquadOwner(): LivingEntity? {
		return getSquadLeader()?.owner
	}

	fun getSquadLeader(): ToySoldierEntity? {
		if (isSquadLeader) return this

		val leaderUuid = squadLeaderUuid ?: return null
		val nearbySoldiers = level().getEntities(
			this,
			boundingBox.inflate(40.0 * getAttributeValue(Attributes.SCALE))
		).filterIsInstance<ToySoldierEntity>()

		return nearbySoldiers.firstOrNull { it.uuid == leaderUuid }
	}

	fun setSquadLeader(toySoldierEntity: ToySoldierEntity?): Boolean {
		if (toySoldierEntity == null) {
			squadLeaderUuid = null
			return true
		}

		if (toySoldierEntity == this) return false
		if (!toySoldierEntity.isSquadLeader) return false

		squadLeaderUuid = toySoldierEntity.uuid
		return true
	}

	fun getChildren(): List<ToySoldierEntity> {
		if (!isSquadLeader) return emptyList()

		val nearbySoldiers = level().getEntities(
			this,
			boundingBox.inflate(40.0 * getAttributeValue(Attributes.SCALE))
		).filterIsInstance<ToySoldierEntity>()

		return nearbySoldiers.filter { it.squadLeaderUuid == this.uuid }
	}

	fun getSquadMembers(): List<ToySoldierEntity> {
		val leader = getSquadLeader() ?: return emptyList()

		return leader.getChildren() + leader
	}

	fun getStack(): ItemStack {
		val component = ToySoldierDataComponent.fromEntity(this)
		return ModItems.TOY_SOLDIER.withComponent(ModDataComponents.TOY_SOLDIER.get(), component)
	}

	override fun interactAt(player: Player, vec: Vec3, hand: InteractionHand): InteractionResult {
		if (
			isClientSide
			|| !player.isSecondaryUseActive
			|| hand != InteractionHand.MAIN_HAND
			|| player != getSquadOwner()
		) return InteractionResult.PASS

		val squad = getSquadMembers()
		for (member in squad) {
			val stack = member.getStack()
			if (player.giveOrDropStack(stack)) {
				member.discard()
			}
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		fun setAttributes(): AttributeSupplier {
			return createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.ATTACK_DAMAGE, 2.0)
				.add(Attributes.ATTACK_SPEED, 1.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2)
				.build()
		}
	}

}