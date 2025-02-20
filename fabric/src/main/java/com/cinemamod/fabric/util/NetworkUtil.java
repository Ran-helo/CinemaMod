package com.cinemamod.fabric.util;

import com.cinemamod.fabric.CinemaMod;
import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.fabric.gui.VideoHistoryScreen;
import com.cinemamod.fabric.gui.VideoQueueScreen;
import com.cinemamod.fabric.gui.VideoSettingsScreen;
import com.cinemamod.fabric.screen.PreviewScreen;
import com.cinemamod.common.screen.PreviewScreenManager;
import com.cinemamod.fabric.screen.Screen;
import com.cinemamod.common.service.VideoService;
import com.cinemamod.common.util.NetworkUtilCommon;
import com.cinemamod.common.video.Video;
import com.cinemamod.common.video.VideoInfo;
import com.cinemamod.common.video.list.VideoList;
import com.cinemamod.common.video.list.VideoListEntry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

public final class NetworkUtil implements NetworkUtilCommon.INetworkUtilCommon {

    private static final CinemaModCommonClient.ICinemaModCommonClient CD = CinemaModCommonClient.getInstance();
    /* INCOMING */
    private static final ResourceLocation CHANNEL_SERVICES = new ResourceLocation(CinemaMod.MODID, "services");
    private static final ResourceLocation CHANNEL_SCREENS = new ResourceLocation(CinemaMod.MODID, "screens");
    private static final ResourceLocation CHANNEL_LOAD_SCREEN = new ResourceLocation(CinemaMod.MODID, "load_screen");
    private static final ResourceLocation CHANNEL_UNLOAD_SCREEN = new ResourceLocation(CinemaMod.MODID, "unload_screen");
    private static final ResourceLocation CHANNEL_UPDATE_PREVIEW_SCREEN = new ResourceLocation(CinemaMod.MODID, "update_preview_screen");
    private static final ResourceLocation CHANNEL_OPEN_SETTINGS_SCREEN = new ResourceLocation(CinemaMod.MODID, "open_settings_screen");
    private static final ResourceLocation CHANNEL_OPEN_HISTORY_SCREEN = new ResourceLocation(CinemaMod.MODID, "open_history_screen");
    private static final ResourceLocation CHANNEL_OPEN_PLAYLISTS_SCREEN = new ResourceLocation(CinemaMod.MODID, "open_playlists_screen");
    private static final ResourceLocation CHANNEL_VIDEO_LIST_HISTORY_SPLIT = new ResourceLocation(CinemaMod.MODID, "video_list_history_split");
    private static final ResourceLocation CHANNEL_VIDEO_LIST_PLAYLIST_SPLIT = new ResourceLocation(CinemaMod.MODID, "video_list_playlist_split");
    private static final ResourceLocation CHANNEL_VIDEO_QUEUE_STATE = new ResourceLocation(CinemaMod.MODID, "video_queue_state");
    /* OUTGOING */
    private static final ResourceLocation CHANNEL_VIDEO_REQUEST = new ResourceLocation(CinemaMod.MODID, "video_request");
    private static final ResourceLocation CHANNEL_VIDEO_HISTORY_REMOVE = new ResourceLocation(CinemaMod.MODID, "video_history_remove");
    private static final ResourceLocation CHANNEL_VIDEO_QUEUE_VOTE = new ResourceLocation(CinemaMod.MODID, "video_queue_vote");
    private static final ResourceLocation CHANNEL_VIDEO_QUEUE_REMOVE = new ResourceLocation(CinemaMod.MODID, "video_queue_remove");
    private static final ResourceLocation CHANNEL_SHOW_VIDEO_TIMELINE = new ResourceLocation(CinemaMod.MODID, "show_video_timeline");

    public void registerReceivers() {
        NetworkUtilCommon.setInstance(this);
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SERVICES, (client, handler, buf, responseSender) -> {
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                CD.getVideoServiceManager().register(new VideoService().fromBytes(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_SCREENS, (client, handler, buf, responseSender) -> {
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                CD.getScreenManager().registerScreen(new Screen().fromBytes(buf));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_LOAD_SCREEN, (client, handler, buf, responseSender) -> {
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            Screen screen = (Screen) CD.getScreenManager().getScreen(pos);
            if (screen == null) return;
            Video video = new Video().fromBytes(buf);
            client.submit(() -> screen.loadVideo(video));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UNLOAD_SCREEN, (client, handler, buf, responseSender) -> {
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            Screen screen = (Screen) CD.getScreenManager().getScreen(pos);
            if (screen == null) return;
            client.submit(screen::closeBrowser);
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UPDATE_PREVIEW_SCREEN, (client, handler, buf, responseSender) -> {
            PreviewScreenManager manager = CD.getPreviewScreenManager();
            PreviewScreen previewScreen = new PreviewScreen().fromBytes(buf);
            VideoInfo videoInfo = buf.readBoolean() ? new VideoInfo().fromBytes(buf) : null;
            previewScreen.setVideoInfo(videoInfo);
            if (manager.getPreviewScreen(previewScreen.getBlockPos()) == null)
                manager.addPreviewScreen(previewScreen);
            else
                manager.getPreviewScreen(previewScreen.getBlockPos()).setVideoInfo(videoInfo);
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_SETTINGS_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.setScreen(new VideoSettingsScreen()));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_OPEN_HISTORY_SCREEN, (client, handler, buf, responseSender) -> {
            client.submit(() -> client.setScreen(new VideoHistoryScreen()));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_LIST_HISTORY_SPLIT, (client, handler, buf, responseSender) -> {
            List<VideoListEntry> entries = new ArrayList<>();
            int length = buf.readInt();
            for (int i = 0; i < length; i++)
                entries.add(new VideoListEntry().fromBytes(buf));
            CD.getVideoListManager().getHistory().merge(new VideoList(entries));
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_LIST_PLAYLIST_SPLIT, (client, handler, buf, responseSender) -> {
            // TODO:
        });
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_VIDEO_QUEUE_STATE, (client, handler, buf, responseSender) -> {
            CD.getVideoQueue().fromBytes(buf);
            client.submit(() -> {
                if (client.screen instanceof VideoQueueScreen) {
                    VideoQueueScreen videoQueueScreen = (VideoQueueScreen) client.screen;
                    videoQueueScreen.videoQueueWidget.update();
                }
            });
        });
    }

    public void sendVideoRequestPacket(VideoInfo videoInfo) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        ClientPlayNetworking.send(CHANNEL_VIDEO_REQUEST, buf);
    }

    public void sendDeleteHistoryPacket(VideoInfo videoInfo) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        ClientPlayNetworking.send(CHANNEL_VIDEO_HISTORY_REMOVE, buf);
    }

    public void sendVideoQueueVotePacket(VideoInfo videoInfo, int voteType) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        buf.writeInt(voteType);
        ClientPlayNetworking.send(CHANNEL_VIDEO_QUEUE_VOTE, buf);
    }

    public void sendVideoQueueRemovePacket(VideoInfo videoInfo) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        videoInfo.toBytes(buf);
        ClientPlayNetworking.send(CHANNEL_VIDEO_QUEUE_REMOVE, buf);
    }

    public void sendShowVideoTimelinePacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(CHANNEL_SHOW_VIDEO_TIMELINE, buf);
    }

}
