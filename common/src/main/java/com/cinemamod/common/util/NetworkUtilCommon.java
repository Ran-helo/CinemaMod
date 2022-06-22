package com.cinemamod.common.util;

import com.cinemamod.common.video.VideoInfo;

public class NetworkUtilCommon {
    private static INetworkUtilCommon instance;

    public static INetworkUtilCommon getInstance() {
        return instance;
    }

    public static void setInstance(INetworkUtilCommon instance) {
        NetworkUtilCommon.instance = instance;
    }

    public interface INetworkUtilCommon {
        void sendVideoRequestPacket(VideoInfo videoInfo);

        void sendDeleteHistoryPacket(VideoInfo videoInfo);

        void sendVideoQueueVotePacket(VideoInfo videoInfo, int voteType);

        void sendVideoQueueRemovePacket(VideoInfo videoInfo);

        void sendShowVideoTimelinePacket();
    }
}
