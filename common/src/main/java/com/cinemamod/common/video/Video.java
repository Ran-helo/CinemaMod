package com.cinemamod.common.video;

import com.cinemamod.common.buffer.PacketByteBufSerializable;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.NotImplementedException;

public class Video implements PacketByteBufSerializable<Video> {

    private VideoInfo videoInfo;
    private long startedAt;

    public Video(VideoInfo videoInfo, long startedAt) {
        this.videoInfo = videoInfo;
        this.startedAt = startedAt;
    }

    public Video() {

    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public long getStartedAt() {
        return startedAt;
    }

    @Override
    public Video fromBytes(FriendlyByteBuf buf) {
        videoInfo = new VideoInfo().fromBytes(buf);
        if (videoInfo == null) return null;
        startedAt = buf.readLong();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
