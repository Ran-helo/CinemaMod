package com.cinemamod.fabric.gui.widget;

import com.cinemamod.common.util.NetworkUtilCommon;
import com.cinemamod.common.video.list.VideoListEntry;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratableEntry;

public class VideoHistoryListWidgetEntry extends VideoListWidgetEntry {

    public VideoHistoryListWidgetEntry(VideoListWidget parent, VideoListEntry video, Minecraft client) {
        super(parent, video, client);
    }

    @Override
    protected void trashButtonAction(VideoListEntry video) {
        NetworkUtilCommon.getInstance().sendDeleteHistoryPacket(video.getVideoInfo());
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return null;
    }

}
