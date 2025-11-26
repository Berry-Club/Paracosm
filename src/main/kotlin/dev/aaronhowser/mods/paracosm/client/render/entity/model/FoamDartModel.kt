package dev.aaronhowser.mods.paracosm.client.render.entity.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.aaronhowser.mods.paracosm.entity.FoamDartProjectile
import dev.aaronhowser.mods.paracosm.util.OtherUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class FoamDartModel(
	root: ModelPart
) : EntityModel<FoamDartProjectile>() {

	private val main: ModelPart = root.getChild("main")

	override fun setupAnim(
		entity: FoamDartProjectile,
		limbSwing: Float,
		limbSwingAmount: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
	}

	override fun renderToBuffer(
		poseStack: PoseStack,
		buffer: VertexConsumer,
		packedLight: Int,
		packedOverlay: Int,
		color: Int
	) {
		main.render(
			poseStack,
			buffer,
			packedLight, packedOverlay,
			color
		)
	}

	companion object {
		val LAYER_LOCATION = ModelLayerLocation(OtherUtil.modResource("foam_dart"), "main")

		fun createBodyLayer(): LayerDefinition {
			val meshDefinition = MeshDefinition()
			val partDefinition = meshDefinition.root

			partDefinition.addOrReplaceChild(
				"main",
				CubeListBuilder
					.create()
					.texOffs(0, 0)
					.addBox(
						-0.5f, -1f, -8f,
						1f, 1f, 5f,
						CubeDeformation(0f)
					),
				PartPose.offset(0f, 0f, 0f)
			)

			return LayerDefinition.create(meshDefinition, 16, 16)
		}
	}

}