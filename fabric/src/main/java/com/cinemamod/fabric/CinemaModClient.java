package com.cinemamod.fabric;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.common.VideoSettings;
import com.cinemamod.common.WindowFocusMuteThread;
import com.cinemamod.fabric.block.PreviewScreenBlock;
import com.cinemamod.fabric.block.PreviewScreenBlockEntity;
import com.cinemamod.fabric.block.ScreenBlock;
import com.cinemamod.fabric.block.ScreenBlockEntity;
import com.cinemamod.fabric.block.render.PreviewScreenBlockEntityRenderer;
import com.cinemamod.fabric.block.render.ScreenBlockEntityRenderer;
import com.cinemamod.fabric.cef.CefUtil;
import com.cinemamod.fabric.gui.VideoQueueScreen;
import com.cinemamod.fabric.gui.VideoRequestBrowser;
import com.cinemamod.common.screen.PreviewScreenManager;
import com.cinemamod.common.screen.ScreenManager;
import com.cinemamod.common.service.VideoServiceManager;
import com.cinemamod.fabric.util.NetworkUtil;
import com.cinemamod.common.video.list.VideoListManager;
import com.cinemamod.common.video.queue.VideoQueue;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.Util;
import org.cef.OS;

import java.io.IOException;

public class CinemaModClient implements ClientModInitializer, CinemaModCommonClient.ICinemaModCommonClient {

    private VideoServiceManager videoServiceManager;
    private ScreenManager screenManager;
    private PreviewScreenManager previewScreenManager;
    private VideoSettings videoSettings;
    private VideoListManager videoListManager;
    private VideoQueue videoQueue;

    public VideoServiceManager getVideoServiceManager() {
        return videoServiceManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public PreviewScreenManager getPreviewScreenManager() {
        return previewScreenManager;
    }

    public VideoSettings getVideoSettings() {
        return videoSettings;
    }

    public VideoListManager getVideoListManager() {
        return videoListManager;
    }

    public VideoQueue getVideoQueue() {
        return videoQueue;
    }

    static void initCefMac() {
        if (OS.isMacintosh()) {
            Util.bootstrapExecutor().execute(() -> {
                if (CefUtil.init()) {
                    com.cinemamod.common.CinemaMod.LOGGER.info("Chromium Embedded Framework initialized for macOS");
                } else {
                    com.cinemamod.common.CinemaMod.LOGGER.warn("Could not initialize Chromium Embedded Framework for macOS");
                }
            });
        }
    }

    @Override
    public void onInitializeClient() {
        CinemaModCommonClient.setInstance(this);

        // Hack for initializing CEF on macos
        initCefMac();

        CefUtil.registerCefTick();

        // Register ScreenBlock
        ScreenBlock.register();
        ScreenBlockEntity.register();
        ScreenBlockEntityRenderer.register();

        // Register PreviewScreenBlock
        PreviewScreenBlock.register();
        PreviewScreenBlockEntity.register();
        PreviewScreenBlockEntityRenderer.register();

        new NetworkUtil().registerReceivers();

        videoServiceManager = new VideoServiceManager();
        screenManager = new ScreenManager();
        previewScreenManager = new PreviewScreenManager();
        videoSettings = new VideoSettings();
        videoListManager = new VideoListManager();
        videoQueue = new VideoQueue();

        try {
            videoSettings.load();
        } catch (IOException e) {
            e.printStackTrace();
            CinemaMod.LOGGER.warn("Could not load video settings.");
        }

        new WindowFocusMuteThread().start();

        VideoQueueScreen.registerKeyInput();
        VideoRequestBrowser.registerKeyInput();
        VideoRequestBrowser.registerCefTick();
    }

}
