package com.cinemamod.fabric.gui.widget;

import com.cinemamod.fabric.CinemaMod;
import com.cinemamod.fabric.CinemaModClient;
import com.cinemamod.fabric.gui.VideoQueueScreen;
import com.cinemamod.fabric.util.NetworkUtil;
import com.cinemamod.fabric.video.queue.QueuedVideo;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.gui.GuiComponent.blit;
import static net.minecraft.client.gui.GuiComponent.fill;
import static net.minecraft.client.gui.screens.social.PlayerEntry.*;

public class VideoQueueWidgetEntry extends ContainerObjectSelectionList.Entry<VideoQueueWidgetEntry> implements Comparable<VideoQueueWidgetEntry> {

    private static final ResourceLocation UPVOTE_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/upvote.png");
    private static final ResourceLocation UPVOTE_SELECTED_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/upvote_selected.png");
    private static final ResourceLocation UPVOTE_ACTIVE_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/upvote_active.png");
    private static final ResourceLocation DOWNVOTE_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/downvote.png");
    private static final ResourceLocation DOWNVOTE_SELECTED_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/downvote_selected.png");
    private static final ResourceLocation DOWNVOTE_ACTIVE_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/downvote_active.png");
    private static final ResourceLocation TRASH_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/trash.png");
    private static final ResourceLocation TRASH_SELECTED_TEXTURE = new ResourceLocation(CinemaMod.MODID, "textures/gui/trash_selected.png");

    private final VideoQueueScreen parent;
    private final QueuedVideo queuedVideo;
    private final List<GuiEventListener> children;
    protected Minecraft client;
    private boolean downVoteButtonSelected;
    private boolean upVoteButtonSelected;
    private boolean trashButtonSelected;

    public VideoQueueWidgetEntry(VideoQueueScreen parent, QueuedVideo queuedVideo, Minecraft client) {
        this.parent = parent;
        this.queuedVideo = queuedVideo;
        children = ImmutableList.of();
        this.client = client;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int m = y + (entryHeight - 7) / 2;
        int color = queuedVideo.isOwner() ? SKIN_SHADE : BG_FILL;
        fill(matrices, x, y, x + entryWidth, y + entryHeight, color);
        client.font.draw(matrices, queuedVideo.getVideoInfo().getTitleShort(), (float) i, (float) m, PLAYERNAME_COLOR);
        client.font.draw(matrices, queuedVideo.getVideoInfo().getDurationString(), (float) i + 118, (float) m, PLAYERNAME_COLOR);
        client.font.draw(matrices, queuedVideo.getScoreString(), (float) i + 165, (float) m, PLAYERNAME_COLOR);
        renderDownVoteButton(matrices, mouseX, mouseY, i, j);
        renderUpVoteButton(matrices, mouseX, mouseY, i, j);
        renderTrashButton(matrices, mouseX, mouseY, i, j);
        if (mouseX > i && mouseX < i + 180 && mouseY > j && mouseY < j + 18) {
            parent.renderTooltip(matrices, Component.nullToEmpty(queuedVideo.getVideoInfo().getTitle()), mouseX, mouseY);
        }
    }

    private void renderDownVoteButton(PoseStack matrices, int mouseX, int mouseY, int i, int j) {
        int downVoteButtonPosX = i + 185;
        int downVoteButtonPosY = j + 7;

        downVoteButtonSelected = mouseX > downVoteButtonPosX && mouseX < downVoteButtonPosX + 12 && mouseY > downVoteButtonPosY && mouseY < downVoteButtonPosY + 12;

        if (queuedVideo.getClientState() == -1) {
            RenderSystem.setShaderTexture(0, DOWNVOTE_ACTIVE_TEXTURE);
        } else if (downVoteButtonSelected) {
            RenderSystem.setShaderTexture(0, DOWNVOTE_SELECTED_TEXTURE);
        } else {
            RenderSystem.setShaderTexture(0, DOWNVOTE_TEXTURE);
        }

        blit(matrices, downVoteButtonPosX, downVoteButtonPosY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

    private void renderUpVoteButton(PoseStack matrices, int mouseX, int mouseY, int i, int j) {
        int upVoteButtonPosX = i + 200;
        int upVoteButtonPosY = j + 3;

        upVoteButtonSelected = mouseX > upVoteButtonPosX && mouseX < upVoteButtonPosX + 12 && mouseY > upVoteButtonPosY && mouseY < upVoteButtonPosY + 12;

        if (queuedVideo.getClientState() == 1) {
            RenderSystem.setShaderTexture(0, UPVOTE_ACTIVE_TEXTURE);
        } else if (upVoteButtonSelected) {
            RenderSystem.setShaderTexture(0, UPVOTE_SELECTED_TEXTURE);
        } else {
            RenderSystem.setShaderTexture(0, UPVOTE_TEXTURE);
        }

        blit(matrices, upVoteButtonPosX, upVoteButtonPosY, 12, 12, 32F, 32F, 8, 8, 8, 8);
    }

    private void renderTrashButton(PoseStack matrices, int mouseX, int mouseY, int i, int j) {
        if (queuedVideo.isOwner()) {
            int trashButtonPosX = i + 225;
            int trashButtonPosY = j + 5;

            trashButtonSelected = mouseX > trashButtonPosX && mouseX < trashButtonPosX + 12 && mouseY > trashButtonPosY && mouseY < trashButtonPosY + 12;

            if (trashButtonSelected) {
                RenderSystem.setShaderTexture(0, TRASH_SELECTED_TEXTURE);
            } else {
                RenderSystem.setShaderTexture(0, TRASH_TEXTURE);
            }

            blit(matrices, trashButtonPosX, trashButtonPosY, 12, 12, 32F, 32F, 8, 8, 8, 8);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (downVoteButtonSelected) {
            NetworkUtil.sendVideoQueueVotePacket(queuedVideo.getVideoInfo(), -1);
        } else if (upVoteButtonSelected) {
            NetworkUtil.sendVideoQueueVotePacket(queuedVideo.getVideoInfo(), 1);
        } else if (trashButtonSelected) {
            NetworkUtil.sendVideoQueueRemovePacket(queuedVideo.getVideoInfo());
        }

        return true;
    }

    @Override
    public int compareTo(@NotNull VideoQueueWidgetEntry videoQueueWidgetEntry) {
        return queuedVideo.compareTo(videoQueueWidgetEntry.queuedVideo);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return null;
    }

}
