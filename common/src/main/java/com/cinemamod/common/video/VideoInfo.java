package com.cinemamod.common.video;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.common.buffer.PacketByteBufSerializable;
import com.cinemamod.common.service.VideoService;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;

public class VideoInfo implements PacketByteBufSerializable<VideoInfo> {

    private VideoService videoService;
    private String id;
    private String title;
    private String poster;
    private String thumbnailUrl;
    private long durationSeconds;

    public VideoInfo(VideoService videoService, String id) {
        this.videoService = videoService;
        this.id = id;
    }

    public VideoInfo() {

    }

    public VideoService getVideoService() {
        return videoService;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        if (title.length() > 23) {
            return title.substring(0, 20) + "...";
        } else {
            return title;
        }
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(@Nullable String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getDurationString() {
        long totalDurationMillis = durationSeconds * 1000;
        String totalDurationFormatted = DurationFormatUtils.formatDuration(totalDurationMillis, "H:mm:ss");
        totalDurationFormatted = reduceFormattedDuration(totalDurationFormatted);
        return totalDurationFormatted;
    }

    private static String reduceFormattedDuration(String formatted) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = formatted.split(":");

        // If does not have hours
        if (!split[0].equals("0")) {
            return formatted;
        } else {
            stringBuilder.append(split[1]).append(":").append(split[2]);
            return stringBuilder.toString();
        }
    }

    public boolean isLivestream() {
        return durationSeconds == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VideoInfo)) {
            return false;
        }
        VideoInfo videoInfo = (VideoInfo) o;
        return videoService == videoInfo.videoService && Objects.equals(id, videoInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoService, id);
    }

    @Override
    public VideoInfo fromBytes(FriendlyByteBuf buf) {
        videoService = CinemaModCommonClient.getInstance().getVideoServiceManager().getByName(buf.readUtf());
        if (videoService == null) return null;
        id = buf.readUtf();
        title = buf.readUtf();
        poster = buf.readUtf();
        thumbnailUrl = buf.readUtf();
        durationSeconds = buf.readLong();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(videoService.getName());
        buf.writeUtf(id);
        buf.writeUtf(title);
        buf.writeUtf(poster);
        buf.writeUtf(thumbnailUrl);
        buf.writeLong(durationSeconds);
    }

}
