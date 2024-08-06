package dev.aaronhowser.mods.paracosm.client.render.curio

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.client.ICurioRenderer

class SeeingStoneCurioRenderer : ICurioRenderer {

    //TODO
    override fun <T : LivingEntity, M : EntityModel<T>> render(
        stack: ItemStack,
        slotContext: SlotContext,
        matrixStack: PoseStack,
        renderLayerParent: RenderLayerParent<T, M>,
        renderTypeBuffer: MultiBufferSource,
        light: Int,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        val entity = slotContext.entity

        ICurioRenderer.followHeadRotations(entity)
    }
}