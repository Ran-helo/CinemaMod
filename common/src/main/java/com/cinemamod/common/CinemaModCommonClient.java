package com.cinemamod.common;

import com.cinemamod.common.screen.PreviewScreenManager;
import com.cinemamod.common.screen.ScreenManager;
import com.cinemamod.common.service.VideoServiceManager;
import com.cinemamod.common.video.list.VideoListManager;
import com.cinemamod.common.video.queue.VideoQueue;

public class CinemaModCommonClient {
    private static ICinemaModCommonClient instance;

    public static void setInstance(ICinemaModCommonClient instance) {
        CinemaModCommonClient.instance = instance;
    }

    public static ICinemaModCommonClient getInstance() {
        return instance;
    }

    public interface ICinemaModCommonClient {

        VideoServiceManager getVideoServiceManager();

        ScreenManager getScreenManager();

        PreviewScreenManager getPreviewScreenManager();

        VideoSettings getVideoSettings();

        VideoListManager getVideoListManager();

        VideoQueue getVideoQueue();
    }
}
