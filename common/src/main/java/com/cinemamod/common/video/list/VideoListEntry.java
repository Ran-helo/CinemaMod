package com.cinemamod.common.video.list;

import com.cinemamod.common.buffer.PacketByteBufSerializable;
import com.cinemamod.common.video.VideoInfo;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class VideoListEntry implements Comparable<VideoListEntry>, PacketByteBufSerializable<VideoListEntry> {

    private VideoInfo videoInfo;
    private long lastRequested;
    private int timesRequested;

    public VideoListEntry(VideoInfo videoInfo, long lastRequested, int timesRequested) {
        this.videoInfo = videoInfo;
        this.lastRequested = lastRequested;
        this.timesRequested = timesRequested;
    }

    public VideoListEntry() {

    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public long getLastRequested() {
        return lastRequested;
    }

    public int getTimesRequested() {
        return Math.min(timesRequested, 999);
    }

    @Override
    public int compareTo(@NotNull VideoListEntry entry) {
        if (lastRequested == entry.lastRequested) {
            return 0;
        } else if (lastRequested < entry.lastRequested) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public VideoListEntry fromBytes(FriendlyByteBuf buf) {
        videoInfo = new VideoInfo().fromBytes(buf);
        lastRequested = buf.readLong();
        timesRequested = buf.readInt();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
