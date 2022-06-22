package com.cinemamod.fabric.mixins;

import com.cinemamod.common.CinemaModCommonClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class UnloadScreensMixin {

    @Shadow
    private ClientLevel level;

    @Inject(at = @At("HEAD"), method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V")
    private void disconnect(Screen screen, CallbackInfo info) {
        CinemaModCommonClient.getInstance().getScreenManager().unloadAll();
        CinemaModCommonClient.getInstance().getPreviewScreenManager().unloadAll();
        CinemaModCommonClient.getInstance().getVideoServiceManager().unregisterAll();
        CinemaModCommonClient.getInstance().getVideoListManager().reset();
        CinemaModCommonClient.getInstance().getVideoQueue().clear();
    }

}
