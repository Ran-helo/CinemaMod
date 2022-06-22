package com.cinemamod.fabric.gui;

import com.cinemamod.fabric.CinemaModClient;
import com.cinemamod.fabric.gui.widget.VideoHistoryListWidget;
import com.cinemamod.fabric.gui.widget.VideoListWidget;
import com.cinemamod.fabric.video.list.VideoList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class VideoHistoryScreen extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/social_interactions.png");
    protected static final Component SEARCH_TEXT = (new TranslatableComponent("gui.socialInteractions.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

    private EditBox searchBox;
    private VideoListWidget videoListWidget;
    private String currentSearch = "";

    public VideoHistoryScreen() {
        super(Component.nullToEmpty("Video History"));
    }

    @Override
    protected void init() {
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        String string = searchBox != null ? searchBox.getValue() : "";
        searchBox = new EditBox(font, method_31362() + 28, 78, 196, 16, SEARCH_TEXT);
        searchBox.setMaxLength(16);
        searchBox.setBordered(false);
        searchBox.setVisible(true);
        searchBox.setTextColor(16777215);
        searchBox.setValue(string);
        searchBox.setResponder(this::onSearchChange);
        addRenderableWidget(searchBox);
        VideoList videoList = CinemaModClient.getInstance().getVideoListManager().getHistory();
        videoListWidget = new VideoHistoryListWidget(videoList, minecraft, this.width, this.height, 88, this.method_31361(), 19);
    }

    @Override
    public void removed() {
        minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    public void renderBackground(PoseStack matrices) {
        int i = this.method_31362() + 3;
        super.renderBackground(matrices);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrices, i, 64, 1, 1, 236, 8);
        int j = this.method_31360();
        for (int k = 0; k < j; ++k)
            this.blit(matrices, i, 72 + 16 * k, 1, 10, 236, 16);
        this.blit(matrices, i, 72 + 16 * j, 1, 27, 236, 8);
        this.blit(matrices, i + 10, 76, 243, 1, 12, 12);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        videoListWidget.render(matrices, mouseX, mouseY, delta);
        if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
            drawString(matrices, this.minecraft.font, SEARCH_TEXT, this.searchBox.x, this.searchBox.y, -1);
        } else {
            this.searchBox.render(matrices, mouseX, mouseY, delta);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    private int method_31359() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int method_31360() {
        return this.method_31359() / 16;
    }

    private int method_31361() {
        return 80 + this.method_31360() * 16 - 8;
    }

    private int method_31362() {
        return (this.width - 238) / 2;
    }

    private void onSearchChange(String currentSearch) {
        currentSearch = currentSearch.toLowerCase(Locale.ROOT);
        if (!currentSearch.equals(this.currentSearch)) {
            videoListWidget.setSearch(currentSearch);
            this.currentSearch = currentSearch;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchBox.isFocused()) {
            this.searchBox.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button) || videoListWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.searchBox.isFocused() && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            onClose();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        videoListWidget.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

}
