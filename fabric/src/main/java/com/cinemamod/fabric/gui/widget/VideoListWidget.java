package com.cinemamod.fabric.gui.widget;

import com.cinemamod.common.video.list.VideoList;
import com.cinemamod.common.video.list.VideoListEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

public abstract class VideoListWidget extends ContainerObjectSelectionList<VideoListWidgetEntry> {

    protected final VideoList videoList;
    @Nullable
    private String search;

    public VideoListWidget(VideoList videoList, Minecraft client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.videoList = videoList;
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

    public void update() {
        List<VideoListEntry> entries = videoList.getVideos();
        if (search != null)
            entries.removeIf(entry -> !entry.getVideoInfo().getTitle().toLowerCase(Locale.ROOT).contains(search));
        replaceEntries(getWidgetEntries(entries));
    }

    public void setSearch(@Nullable String search) {
        this.search = search;
        update();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        children().forEach(widgetEntry -> widgetEntry.mouseClicked(mouseX, mouseY, button));
        update();
        return true;
    }

    protected abstract List<VideoListWidgetEntry> getWidgetEntries(List<VideoListEntry> entries);

}
