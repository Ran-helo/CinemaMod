package com.cinemamod.common.screen;

import com.cinemamod.common.video.VideoInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IPreviewScreen {
    public int getX();

    public int getY();

    public int getZ();

    public String getFacing();

    public String getStaticTextureUrl();

    public String getActiveTextureUrl();

    @Nullable
    public VideoInfo getVideoInfo();

    public boolean hasVideoInfo();

    public void setVideoInfo(@Nullable VideoInfo videoInfo);

    public BlockPos getBlockPos();

    @Nullable
    public DynamicTexture getStaticTexture();

    @Nullable
    public DynamicTexture getActiveTexture();

    @Nullable
    public DynamicTexture getThumbnailTexture();

    public void setThumbnailTexture(DynamicTexture thumbnailTexture);

    public void register();

    public void unregister();
}
