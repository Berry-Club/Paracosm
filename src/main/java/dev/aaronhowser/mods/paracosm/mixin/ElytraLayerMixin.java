package dev.aaronhowser.mods.paracosm.mixin;

import dev.aaronhowser.mods.paracosm.registry.ModItems;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraLayer.class)
public class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public void towelCape(ItemStack stack, T entity, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() == ModItems.INSTANCE.getTOWEL_CAPE().get()) {
            cir.setReturnValue(true);
        }
    }
}