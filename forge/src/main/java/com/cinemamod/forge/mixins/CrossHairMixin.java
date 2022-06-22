package com.cinemamod.forge.mixins;

import com.cinemamod.common.CinemaModCommonClient;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class CrossHairMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    public void renderCrosshair(PoseStack matrices, CallbackInfo ci) {
        if (CinemaModCommonClient.getInstance().getScreenManager().hasActiveScreen()
                && CinemaModCommonClient.getInstance().getVideoSettings().isHideCrosshair()) {
            ci.cancel();
        }
    }

}
