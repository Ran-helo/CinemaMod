package com.cinemamod.fabric.gui.widget;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.fabric.gui.VideoQueueScreen;
import com.cinemamod.common.video.queue.QueuedVideo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

public class VideoQueueWidget extends ContainerObjectSelectionList<VideoQueueWidgetEntry> {

    private VideoQueueScreen parent;

    public VideoQueueWidget(VideoQueueScreen parent, Minecraft client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.parent = parent;
        setRenderBackground(false);
        setRenderTopAndBottom(false);
        update();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        double d = minecraft.getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((double) this.getRowLeft() * d), (int) ((double) (this.height - this.y1) * d), (int) ((double) (this.getScrollbarPosition() + 6) * d), (int) ((double) (this.height - (this.height - this.y1) - this.y0 - 4) * d));
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        children().forEach(child -> child.mouseClicked(mouseX, mouseY, button));
        return true;
    }

    public void update() {
        List<VideoQueueWidgetEntry> entries = new ArrayList<>();
        List<QueuedVideo> queuedVideos = CinemaModCommonClient.getInstance().getVideoQueue().getVideos();
        Collections.sort(queuedVideos);
        for (int i = 0; i < queuedVideos.size(); i++) {
            entries.add(new VideoQueueWidgetEntry(parent, queuedVideos.get(i), minecraft));
        }
        replaceEntries(entries);
    }

}
