package com.cinemamod.fabric.mixins;

import com.cinemamod.fabric.CinemaModClient;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ItemDropKeyMixin {

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable cbi) {
        if (CinemaModClient.getInstance().getScreenManager().hasActiveScreen()) {
            cbi.cancel();
        }
    }

}
