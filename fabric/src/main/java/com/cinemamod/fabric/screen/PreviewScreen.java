package com.cinemamod.fabric.screen;

import com.cinemamod.common.screen.IPreviewScreen;
import com.cinemamod.fabric.block.PreviewScreenBlock;
import com.cinemamod.common.buffer.PacketByteBufSerializable;
import com.cinemamod.common.util.ImageUtil;
import com.cinemamod.common.video.VideoInfo;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class PreviewScreen implements PacketByteBufSerializable<PreviewScreen>, IPreviewScreen {

    private int x;
    private int y;
    private int z;
    private String facing;
    private String staticTextureUrl;
    private String activeTextureUrl;

    @Nullable
    private VideoInfo videoInfo;

    private transient BlockPos blockPos; // used as a cache for performance
    private transient boolean unregistered;

    @Nullable
    private transient DynamicTexture staticTexture;
    @Nullable
    private transient DynamicTexture activeTexture;
    @Nullable
    private transient DynamicTexture thumbnailTexture;

    public PreviewScreen(int x, int y, int z, String facing, String staticTextureUrl, String activeTextureUrl) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
        this.staticTextureUrl = staticTextureUrl;
        this.activeTextureUrl = activeTextureUrl;
    }

    public PreviewScreen() {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getFacing() {
        return facing;
    }

    public String getStaticTextureUrl() {
        return staticTextureUrl;
    }

    public String getActiveTextureUrl() {
        return activeTextureUrl;
    }

    @Nullable
    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public boolean hasVideoInfo() {
        return videoInfo != null;
    }

    public void setVideoInfo(@Nullable VideoInfo videoInfo) {
        this.videoInfo = videoInfo;

        if (videoInfo == null || videoInfo.getThumbnailUrl() == null) {
            setThumbnailTexture(null);
        } else {
            ImageUtil.fetchImageTextureFromUrl(videoInfo.getThumbnailUrl()).thenAccept(this::setThumbnailTexture);
        }
    }

    public BlockPos getBlockPos() {
        if (blockPos == null) {
            blockPos = new BlockPos(x, y, z);
        }

        return blockPos;
    }

    @Nullable
    public DynamicTexture getStaticTexture() {
        if (staticTexture == null && staticTextureUrl != null) {
            ImageUtil.fetchImageTextureFromUrl(staticTextureUrl).thenAccept(texture -> staticTexture = texture);
        }

        return staticTexture;
    }

    @Nullable
    public DynamicTexture getActiveTexture() {
        if (activeTexture == null && activeTextureUrl != null) {
            ImageUtil.fetchImageTextureFromUrl(activeTextureUrl).thenAccept(texture -> activeTexture = texture);
        }

        return activeTexture;
    }

    @Nullable
    public DynamicTexture getThumbnailTexture() {
        return thumbnailTexture;
    }

    public void setThumbnailTexture(DynamicTexture thumbnailTexture) {
        if (this.thumbnailTexture != null) {
            this.thumbnailTexture.close();
        }
        this.thumbnailTexture = thumbnailTexture;
    }

    public void register() {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        if (Minecraft.getInstance().level.hasChunk(chunkX, chunkZ)) {
            Minecraft.getInstance().level.setBlockAndUpdate(getBlockPos(), PreviewScreenBlock.PREVIEW_SCREEN_BLOCK.defaultBlockState());
        }

        ClientChunkEvents.CHUNK_LOAD.register((clientWorld, worldChunk) -> {
            if (unregistered) {
                return;
            }

            // If the loaded chunk has this screen block in it, place it in the world
            if (worldChunk.getPos().x == chunkX && worldChunk.getPos().z == chunkZ) {
                clientWorld.setBlockAndUpdate(getBlockPos(), PreviewScreenBlock.PREVIEW_SCREEN_BLOCK.defaultBlockState());
            }
        });
    }

    public void unregister() {
        unregistered = true;

        if (staticTexture != null) staticTexture.close();
        if (activeTexture != null) activeTexture.close();
        if (thumbnailTexture != null) thumbnailTexture.close();

        if (Minecraft.getInstance().level != null) {
            Minecraft.getInstance().level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    public PreviewScreen fromBytes(FriendlyByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        facing = buf.readUtf();
        staticTextureUrl = buf.readUtf();
        activeTextureUrl = buf.readUtf();
        return this;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        throw new NotImplementedException("Not implemented on client");
    }

}
