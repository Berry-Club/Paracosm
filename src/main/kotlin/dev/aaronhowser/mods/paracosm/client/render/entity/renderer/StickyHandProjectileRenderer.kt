package dev.aaronhowser.mods.paracosm.client.render.entity.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.paracosm.Paracosm
import dev.aaronhowser.mods.paracosm.client.render.entity.model.StickyHandProjectileModel
import dev.aaronhowser.mods.paracosm.entity.projectile.StickyHandProjectile
import dev.aaronhowser.mods.paracosm.registry.ModItems
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth.lerp
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.renderer.GeoEntityRenderer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @see net.minecraft.client.renderer.entity.FishingHookRenderer
 */
class StickyHandProjectileRenderer(
	context: EntityRendererProvider.Context
) : GeoEntityRenderer<StickyHandProjectile>(context, StickyHandProjectileModel()) {

	override fun getTextureLocation(p0: StickyHandProjectile): ResourceLocation = TEXTURE

	override fun render(
		stickyHandEntity: StickyHandProjectile,
		entityYaw: Float,
		partialTick: Float,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int
	) {
		val player = stickyHandEntity.owner as? Player ?: return

		poseStack.pushPose()

		val f = player.getAttackAnim(partialTick)
		val f1 = sin(sqrt(f) * 3.1415927f)

		val vec3: Vec3 = getPlayerHandPos(player, f1, partialTick)
		val vec31: Vec3 = stickyHandEntity.getPosition(partialTick).add(0.0, 0.25, 0.0)

		val f2 = (vec3.x - vec31.x).toFloat()
		val f3 = (vec3.y - vec31.y).toFloat()
		val f4 = (vec3.z - vec31.z).toFloat()
		val vertexConsumer1 = buffer.getBuffer(RenderType.lineStrip())
		val pose1 = poseStack.last()

		for (j in 0..16) {
			stringVertex(
				f2,
				f3,
				f4,
				vertexConsumer1,
				pose1,
				j.toFloat() / 16f,
				(j + 1).toFloat() / 16f
			)
		}

		poseStack.popPose()
		super.render(stickyHandEntity, entityYaw, partialTick, poseStack, buffer, packedLight)
	}

	private fun getPlayerHandPos(player: Player, pFloat: Float, partialTick: Float): Vec3 {
		var i = if (player.mainArm == HumanoidArm.RIGHT) 1 else -1

		val stickyStack = player.mainHandItem
		if (stickyStack.item != ModItems.STICKY_HAND.get()) i = -1

		if (player == AaronClientUtil.localPlayer && entityRenderDispatcher.options.cameraType.isFirstPerson) {
			val d4 = 960.0 / (entityRenderDispatcher.options.fov().get() as Int).toDouble()
			val vec3 = entityRenderDispatcher.camera.nearPlane
				.getPointOnPlane(i.toFloat() * 0.525f, -0.1f)
				.scale(d4)
				.yRot(pFloat * 0.5f)
				.xRot(-pFloat * 0.7f)

			return player.getEyePosition(partialTick).add(vec3)
		}

		val f = lerp(partialTick, player.yBodyRotO, player.yBodyRot) * 0.017453292f
		val d0 = sin(f).toDouble()
		val d1 = cos(f).toDouble()
		val f1 = player.scale
		val d2 = i.toDouble() * 0.35 * f1.toDouble()
		val d3 = 0.8 * f1.toDouble()
		val f2 = if (player.isCrouching) -0.1875f else 0.0f
		return player.getEyePosition(partialTick)
			.add(-d1 * d2 - d0 * d3, f2.toDouble() - 0.45 * f1.toDouble(), -d0 * d2 + d1 * d3)
	}

	private fun stringVertex(
		x: Float,
		y: Float,
		z: Float,
		consumer: VertexConsumer,
		pose: PoseStack.Pose,
		stringFraction: Float,
		nextStringFraction: Float
	) {
		val f = x * stringFraction
		val f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5f + 0.25f
		val f2 = z * stringFraction
		var f3 = x * nextStringFraction - f
		var f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5f + 0.25f - f1
		var f5 = z * nextStringFraction - f2
		val f6 = sqrt(f3 * f3 + f4 * f4 + f5 * f5)
		f3 /= f6
		f4 /= f6
		f5 /= f6
		consumer
			.addVertex(pose, f, f1, f2)
			.setColor(-0x5ECD80)    // The opposite of the color of the sticky hand (this is green, so color is purple)
			.setNormal(pose, f3, f4, f5)
	}

	companion object {
		val TEXTURE: ResourceLocation = Paracosm.modResource("textures/entity/sticky_hand.png")
		val RENDER_TYPE: RenderType = RenderType.entityCutout(TEXTURE)
	}

}