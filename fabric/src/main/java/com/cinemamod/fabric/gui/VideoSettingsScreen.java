package com.cinemamod.fabric.gui;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.common.screen.IScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class VideoSettingsScreen extends Screen {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/social_interactions.png");
    private boolean shouldReloadScreen;

    public VideoSettingsScreen() {
        super(Component.nullToEmpty("Video Settings"));
    }

    @Override
    protected void init() {
        addRenderableWidget(new AbstractSliderButton(method_31362() + 23, 78, 196, 20, Component.nullToEmpty("Volume"),
                CinemaModCommonClient.getInstance().getVideoSettings().getVolume()) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                for (IScreen screen : CinemaModCommonClient.getInstance().getScreenManager().getScreens())
                    screen.setVideoVolume((float) value);
                CinemaModCommonClient.getInstance().getVideoSettings().setVolume((float) value);
            }
        });
        addRenderableWidget(new Checkbox(method_31362() + 23, 110, 196, 20, Component.nullToEmpty("Mute video while alt-tabbed"),
                CinemaModCommonClient.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
            @Override
            public void onPress() {
                super.onPress();
                CinemaModCommonClient.getInstance().getVideoSettings().setMuteWhenAltTabbed(selected());
            }
        });
        addRenderableWidget(new Checkbox(method_31362() + 23, 142, 196, 20, Component.nullToEmpty("Hide crosshair while video playing"),
                CinemaModCommonClient.getInstance().getVideoSettings().isHideCrosshair()) {
            @Override
            public void onPress() {
                super.onPress();
                CinemaModCommonClient.getInstance().getVideoSettings().setHideCrosshair(selected());
            }
        });
        addRenderableWidget(new Button(method_31362() + 23, 142 + 32, 196, 20,
                Component.nullToEmpty("Screen resolution: " + CinemaModCommonClient.getInstance().getVideoSettings().getBrowserResolution() + "p"), button -> {
            CinemaModCommonClient.getInstance().getVideoSettings().setNextBrowserResolution();
            button.setMessage(Component.nullToEmpty("Screen resolution: " + CinemaModCommonClient.getInstance().getVideoSettings().getBrowserResolution() + "p"));
            shouldReloadScreen = true;
        }, (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Component.nullToEmpty("A higher resolution may decrease FPS"), mouseX, mouseY);
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
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredString(matrices, this.minecraft.font, Component.nullToEmpty("Video Settings"), this.width / 2, 64 - 10, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        CinemaModCommonClient.getInstance().getVideoSettings().saveAsync();
        if (shouldReloadScreen) {
            for (IScreen screen : CinemaModCommonClient.getInstance().getScreenManager().getScreens()) {
                if (screen.hasBrowser()) {
                    screen.reload();
                }
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            onClose();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

}
