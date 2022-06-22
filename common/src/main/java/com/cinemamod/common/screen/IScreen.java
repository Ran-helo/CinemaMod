package com.cinemamod.common.screen;

import com.cinemamod.common.video.Video;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.cef.browser.CefBrowserOsr;

import java.util.List;

public interface IScreen {
    public int getX();

    public int getY();

    public int getZ();

    public BlockPos getPos();

    public String getFacing();

    public float getWidth();

    public float getHeight();

    public boolean isVisible();

    public boolean isMuted();

    public List<IPreviewScreen> getPreviewScreens();

    public void addPreviewScreen(IPreviewScreen previewScreen);

    public CefBrowserOsr getBrowser();

    public boolean hasBrowser();

    public void reload();

    public void loadVideo(Video video);

    public void closeBrowser();

    public Video getVideo();

    public void setVideoVolume(float volume);

    public void startVideo();

    public void seekVideo(int seconds);

    public BlockPos getBlockPos();

    public void register();

    public void unregister();
}
