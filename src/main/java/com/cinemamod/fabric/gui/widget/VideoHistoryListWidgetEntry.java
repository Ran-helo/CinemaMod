package com.cinemamod.fabric.gui.widget;

import com.cinemamod.fabric.util.NetworkUtil;
import com.cinemamod.fabric.video.list.VideoListEntry;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratableEntry;

public class VideoHistoryListWidgetEntry extends VideoListWidgetEntry {

    public VideoHistoryListWidgetEntry(VideoListWidget parent, VideoListEntry video, Minecraft client) {
        super(parent, video, client);
    }

    @Override
    protected void trashButtonAction(VideoListEntry video) {
        NetworkUtil.sendDeleteHistoryPacket(video.getVideoInfo());
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return null;
    }

}
