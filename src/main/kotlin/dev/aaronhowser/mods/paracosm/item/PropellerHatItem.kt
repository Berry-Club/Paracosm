package dev.aaronhowser.mods.paracosm.item

import dev.aaronhowser.mods.paracosm.client.render.armor.PropellerHatRenderer
import dev.aaronhowser.mods.paracosm.handler.KeyHandler
import dev.aaronhowser.mods.paracosm.item.base.IUpgradeableItem
import dev.aaronhowser.mods.paracosm.registry.ModArmorMaterials
import dev.aaronhowser.mods.paracosm.registry.ModDataComponents
import dev.aaronhowser.mods.paracosm.registry.ModItems
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.SingletonGeoAnimatable
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class PropellerHatItem(properties: Properties) : ArmorItem(ModArmorMaterials.PROPELLER_HAT, Type.HELMET, properties), IUpgradeableItem, GeoItem {

	init {
		SingletonGeoAnimatable.registerSyncedAnimatable(this)
	}

	override fun getEquipmentSlot(): EquipmentSlot = EquipmentSlot.HEAD

	override val possibleUpgrades: List<String> = listOf(
		SMOOTH_FLIGHT_UPGRADE,
		BURST_FLIGHT_UPGRADE
	)

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (entity !is Player) return
		val headItem = entity.getItemBySlot(EquipmentSlot.HEAD)
		if (headItem != stack) return

		if (IUpgradeableItem.hasUpgrade(stack, SMOOTH_FLIGHT_UPGRADE)) {
			smoothFlightTick(entity, stack)
		} else if (IUpgradeableItem.hasUpgrade(stack, BURST_FLIGHT_UPGRADE)) {
			burstFlightTick(entity)
		}
	}

	private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)
	override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

	override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
		controllers.add(AnimationController(this, 0) { it.setAndContinue(SPIN_ANIM) })
	}

	override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
		consumer.accept(object : GeoRenderProvider {
			private var rendererCache: PropellerHatRenderer? = null

			override fun <T : LivingEntity> getGeoArmorRenderer(
				livingEntity: T?,
				itemStack: ItemStack,
				equipmentSlot: EquipmentSlot?,
				original: HumanoidModel<T>?
			): HumanoidModel<*> {
				if (this.rendererCache == null) {
					this.rendererCache = PropellerHatRenderer()
				}
				return this.rendererCache!!
			}
		})
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)

		const val SMOOTH_FLIGHT_UPGRADE = "smooth_flight"
		const val BURST_FLIGHT_UPGRADE = "burst_flight"

		//TODO: Make it so that it spins when you move vertically, not just looping always
		val SPIN_ANIM: RawAnimation = RawAnimation.begin().thenLoop("animation.propellerhat.spin")

		//TODO: Gui indicator for both of these

		//TODO: Whizz sound effect
		// What is it called? the thing that spins, i think?
		// Like this but only the high pitched part: https://www.youtube.com/watch?v=asFIJcLfoos
		private fun burstFlightTick(player: Player) {
			if (!KeyHandler.isHoldingSpace(player)
				|| player.cooldowns.isOnCooldown(ModItems.PROPELLER_HAT.get())
			) return

			player.cooldowns.addCooldown(ModItems.PROPELLER_HAT.get(), 20)

			player.addDeltaMovement(
				Vec3(
					0.0,
					1.0,
					0.0
				)
			)

			player.fallDistance = 0f
		}

		//TODO: Helicopter style sounds
		// Or maybe this: https://www.youtube.com/watch?v=n_xR1M3tGck
		private fun smoothFlightTick(player: Player, stack: ItemStack) {
			val movement = player.deltaMovement

			val flightTicksRemaining = stack.getOrDefault(ModDataComponents.PROPELLER_HAT_FLIGHT_TICKS, 20 * 5)

			if (KeyHandler.isHoldingSpace(player)
				&& movement.y < 1
				&& !player.cooldowns.isOnCooldown(ModItems.PROPELLER_HAT.get())
			) {
				player.addDeltaMovement(
					Vec3(
						0.0,
						player.gravity * 1.2,
						0.0
					)
				)

				player.fallDistance = 0f

				val newAmount = flightTicksRemaining - 1
				if (newAmount <= 0) {
					player.cooldowns.addCooldown(ModItems.PROPELLER_HAT.get(), 20 * 3)
					// TODO: Play a sound, like a helicopter running out of fuel
				}

				stack.set(ModDataComponents.PROPELLER_HAT_FLIGHT_TICKS, newAmount)
			} else {
				val newAmount = minOf(flightTicksRemaining + 2, 20 * 5)
				stack.set(ModDataComponents.PROPELLER_HAT_FLIGHT_TICKS, newAmount)
			}
		}
	}

}