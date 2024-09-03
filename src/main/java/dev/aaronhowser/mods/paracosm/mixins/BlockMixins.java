package dev.aaronhowser.mods.paracosm.mixins;

import dev.aaronhowser.mods.paracosm.item.pogo_stick.CommonBounceHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixins {

    @Inject(method = "updateEntityAfterFallOn", at = @At("HEAD"), cancellable = true)
    private void bounceUp(BlockGetter level, Entity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (CommonBounceHandler.INSTANCE.isBouncing(player)) {
                ci.cancel();
            }
        }
    }

}
