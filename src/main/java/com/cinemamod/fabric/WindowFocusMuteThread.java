package com.cinemamod.fabric;

import com.cinemamod.fabric.screen.Screen;
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
            if (CinemaModClient.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
                if (Minecraft.getInstance().isWindowActive() && !previousState) {
                    // if currently focused and was previously not focused
                    for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
                        screen.setVideoVolume(CinemaModClient.getInstance().getVideoSettings().getVolume());
                    }
                } else if (!Minecraft.getInstance().isWindowActive() && previousState) {
                    // if not focused and was previous focused
                    for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
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
