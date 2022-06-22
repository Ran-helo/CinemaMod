package com.cinemamod.fabric.gui;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.fabric.gui.widget.VideoQueueWidget;
import com.cinemamod.common.util.NetworkUtilCommon;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class VideoQueueScreen extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/social_interactions.png");
    protected static KeyMapping keyBinding;

    public VideoQueueWidget videoQueueWidget;

    public VideoQueueScreen() {
        super(Component.nullToEmpty("Video Queue"));
    }

    @Override
    protected void init() {
        videoQueueWidget = new VideoQueueWidget(this, minecraft, this.width, this.height, 68, this.method_31361(), 19);
        addRenderableWidget(new Button(method_31362() + 23, method_31359() + 78, 196, 20, Component.nullToEmpty("Video Settings"), button -> {
            minecraft.setScreen(new VideoSettingsScreen());
        }));
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

    public void renderBackground(PoseStack matrices) {
        int i = this.method_31362() + 3;
        super.renderBackground(matrices);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrices, i, 64, 1, 1, 236, 8);
        int j = this.method_31360();
        for (int k = 0; k < j; ++k)
            this.blit(matrices, i, 72 + 16 * k, 1, 10, 236, 16);
        this.blit(matrices, i, 72 + 16 * j, 1, 27, 236, 8);
        drawCenteredString(matrices, this.minecraft.font, Component.nullToEmpty("Video Queue - " + videoQueueWidget.children().size() + " entries"), this.width / 2, 64 - 10, -1);
        if (videoQueueWidget.children().isEmpty()) {
            drawCenteredString(matrices, this.minecraft.font, Component.nullToEmpty("No videos queued"), this.width / 2, (56 + this.method_31361()) / 2, -1);
        } else {
            if (videoQueueWidget.getScrollAmount() == 0f) {
                drawCenteredString(matrices, this.minecraft.font, Component.nullToEmpty("UP NEXT ->"), -158 + this.width / 2, 64 + 12, -1);
            }
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        videoQueueWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        NetworkUtilCommon.getInstance().sendShowVideoTimelinePacket();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button) || videoQueueWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyBinding.matches(keyCode, scanCode)) {
            onClose();
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        videoQueueWidget.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public static void registerKeyInput() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.cinemamod.openvideoqueue",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.cinemamod.keybinds"
        ));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!CinemaModCommonClient.getInstance().getScreenManager().hasActiveScreen()) return;

            if (keyBinding.consumeClick()) {
                client.setScreen(new VideoQueueScreen());
            }
        });
    }

}
