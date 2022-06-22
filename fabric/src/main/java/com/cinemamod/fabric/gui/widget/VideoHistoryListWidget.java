package com.cinemamod.fabric.gui.widget;

import com.cinemamod.common.video.list.VideoList;
import com.cinemamod.common.video.list.VideoListEntry;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;

public class VideoHistoryListWidget extends VideoListWidget {

    public VideoHistoryListWidget(VideoList videoList, Minecraft client, int width, int height, int top, int bottom, int itemHeight) {
        super(videoList, client, width, height, top, bottom, itemHeight);
    }

    @Override
    protected List<VideoListWidgetEntry> getWidgetEntries(List<VideoListEntry> entries) {
        return entries.stream()
                .map(entry -> new VideoHistoryListWidgetEntry(this, entry, minecraft))
                .sorted()
                .collect(Collectors.toList());
    }

}
