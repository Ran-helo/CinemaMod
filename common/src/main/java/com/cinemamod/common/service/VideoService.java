package com.cinemamod.common.service;

import com.cinemamod.common.buffer.PacketByteBufSerializable;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.NotImplementedException;

public class VideoService implements PacketByteBufSerializable<VideoService> {

    private String name;
    private String url;
    private String setVolumeJs;
    private String startJs;
    private String seekJs;

    public VideoService(String name, String url, String setVolumeJs, String startJs, String seekJs) {
        this.name = name;
        this.url = url;
        this.setVolumeJs = setVolumeJs;
        this.startJs = startJs;
        this.seekJs = seekJs;
    }

    public VideoService() {

    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSetVolumeJs() {
        return setVolumeJs;
    }

    public String getStartJs() {
        return startJs;
    }

    public String getSeekJs() {
        return seekJs;
    }

    @Override
    public VideoService fromBytes(FriendlyByteBuf buf) {
        name = buf.readUtf();
        url = buf.readUtf();
        setVolumeJs = buf.readUtf();
        startJs = buf.readUtf();
        seekJs = buf.readUtf();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
