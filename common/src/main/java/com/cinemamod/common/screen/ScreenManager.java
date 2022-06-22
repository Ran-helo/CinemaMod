package com.cinemamod.common.screen;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;

public class ScreenManager {

    private final ConcurrentHashMap<BlockPos, IScreen> screens;

    public ScreenManager() {
        screens = new ConcurrentHashMap<>();
    }

    public Collection<IScreen> getScreens() {
        return screens.values();
    }

    public void registerScreen(IScreen screen) {
        if (screens.containsKey(screen.getPos())) {
            IScreen old = screens.get(screen.getPos());
            old.unregister();
            old.closeBrowser();
        }

        screen.register();

        screens.put(screen.getPos(), screen);
    }

    public IScreen getScreen(BlockPos pos) {
        return screens.get(pos);
    }

    // Used for CefClient LoadHandler
    public IScreen getScreen(int browserId) {
        for (IScreen screen : screens.values()) {
            if (screen.hasBrowser()) {
                if (screen.getBrowser().getIdentifier() == browserId) {
                    return screen;
                }
            }
        }

        return null;
    }

    public boolean hasActiveScreen() {
        for (IScreen screen : screens.values()) {
            if (screen.hasBrowser()) {
                return true;
            }
        }

        return false;
    }

    public void unloadAll() {
        for (IScreen screen : screens.values()) {
            screen.closeBrowser();
            screen.unregister();
        }

        screens.clear();
    }

    public void updateAll() {
        for (IScreen screen : screens.values()) {
            if (screen.hasBrowser()) {
                screen.getBrowser().update();
            }
        }
    }

}
