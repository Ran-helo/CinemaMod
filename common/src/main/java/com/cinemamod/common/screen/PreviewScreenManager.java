package com.cinemamod.common.screen;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;

public class PreviewScreenManager {

    private Map<BlockPos, IPreviewScreen> previewScreens;

    public PreviewScreenManager() {
        previewScreens = new HashMap<>();
    }

    public void addPreviewScreen(IPreviewScreen previewScreen) {
        if (previewScreens.containsKey(previewScreen.getBlockPos())) {
            previewScreens.remove(previewScreen.getBlockPos()).unregister();
        }

        previewScreen.register();

        previewScreens.put(previewScreen.getBlockPos(), previewScreen);
    }

    public IPreviewScreen getPreviewScreen(BlockPos blockPos) {
        return previewScreens.get(blockPos);
    }

    public void unloadAll() {
        previewScreens.values().forEach(IPreviewScreen::unregister);
        previewScreens.clear();
    }

}
