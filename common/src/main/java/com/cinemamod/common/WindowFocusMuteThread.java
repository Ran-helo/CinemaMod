package com.cinemamod.common;

import com.cinemamod.common.screen.IScreen;
import net.minecraft.client.Minecraft;

public class WindowFocusMuteThread extends Thread {

    private boolean previousState;

    public WindowFocusMuteThread() {
        setDaemon(true);
        setName("window-focus-cef-mute-thread");
    }

    @Override
    public void run() {
        while (Minecraft.getInstance().isRunning()) {
            if (CinemaModCommonClient.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
                if (Minecraft.getInstance().isWindowActive() && !previousState) {
                    // if currently focused and was previously not focused
                    for (IScreen screen : CinemaModCommonClient.getInstance().getScreenManager().getScreens()) {
                        screen.setVideoVolume(CinemaModCommonClient.getInstance().getVideoSettings().getVolume());
                    }
                } else if (!Minecraft.getInstance().isWindowActive() && previousState) {
                    // if not focused and was previous focused
                    for (IScreen screen : CinemaModCommonClient.getInstance().getScreenManager().getScreens()) {
                        screen.setVideoVolume(0f);
                    }
                }

                previousState = Minecraft.getInstance().isWindowActive();
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
