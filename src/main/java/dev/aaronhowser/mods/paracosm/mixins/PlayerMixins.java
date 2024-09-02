package dev.aaronhowser.mods.paracosm.mixins;

import dev.aaronhowser.mods.paracosm.item.pogo_stick.BounceHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixins {

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void checkIfPogoBouncing(CallbackInfo ci) {
        if (BounceHandler.INSTANCE.isBouncing((Player) (Object) this)) {
            ci.cancel();
        }
    }

}
