package com.cinemamod.fabric.gui;

import com.cinemamod.fabric.cef.CefUtil;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.cef.browser.CefBrowserOsr;
import org.lwjgl.glfw.GLFW;

public class VideoRequestBrowser extends Screen {

    protected static KeyMapping keyBinding;
    private static CefBrowserOsr browser;
    private static final int browserDrawOffset = 40;

    private Button backBtn, fwdBtn, requestBtn, closeBtn;
    private EditBox urlField;

    protected VideoRequestBrowser() {
        super(Component.nullToEmpty("Video Request Browser"));
    }

    @Override
    protected void init() {
        super.init();

        if (CefUtil.isInit() && browser == null) {
            browser = CefUtil.createBrowser("https://google.com", width, height);
        }

        if (browser == null) return;

        browser.resize(minecraft.getWindow().getScreenWidth(), minecraft.getWindow().getScreenHeight() - scaleY(20));

        addRenderableWidget(backBtn = (new Button(browserDrawOffset, browserDrawOffset - 20, 20, 20, new TextComponent("<"), button -> {
            System.out.println("back button");
        })));
        addRenderableWidget(fwdBtn = (new Button(browserDrawOffset + 20, browserDrawOffset - 20, 20, 20, new TextComponent(">"), button -> {
            System.out.println("fwd button");
        })));
        addRenderableWidget(requestBtn = (new Button(width - browserDrawOffset - 20 - 60, browserDrawOffset - 20, 60, 20, new TextComponent("Request"), button -> {
            System.out.println("request button");
        })));
        addRenderableWidget(closeBtn = (new Button(width - browserDrawOffset - 20, browserDrawOffset - 20, 20, 20, new TextComponent("X"), button -> {
            System.out.println("close button");
        })));

        urlField = new EditBox(minecraft.font, browserDrawOffset + 40, browserDrawOffset - 20 + 1, width - browserDrawOffset - 160, 20, new TextComponent(""));
        urlField.setMaxLength(65535);
        urlField.setValue(browser.getURL()); // why does getURL return an empty string here?
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        urlField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        int glId = browser.renderer_.texture_id_[0];
        RenderSystem.setShaderTexture(0, glId);
        Tesselator t = Tesselator.getInstance();
        BufferBuilder buffer = t.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        buffer.vertex(browserDrawOffset, height - browserDrawOffset, 0).color(255, 255, 255, 255).uv(0.0f, 1.0f).endVertex();
        buffer.vertex(width - browserDrawOffset, height - browserDrawOffset, 0).color(255, 255, 255, 255).uv(1.0f, 1.0f).endVertex();
        buffer.vertex(width - browserDrawOffset, browserDrawOffset, 0).color(255, 255, 255, 255).uv(1.0f, 0.0f).endVertex();
        buffer.vertex(browserDrawOffset, browserDrawOffset, 0).color(255, 255, 255, 255).uv(0.0f, 0.0f).endVertex();
        t.end();
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableDepthTest();
        matrices.popPose();
    }

    @Override
    public void onClose() {
        super.onClose();
        browser.close();
        browser = null;
    }

    public int scaleY(int y) {
        assert minecraft != null;
        double sy = ((double) y) / ((double) height) * ((double) minecraft.getWindow().getScreenHeight());
        return (int) sy;
    }

    public int scaleX(int x) {
        assert minecraft != null;
        double sx = ((double) x) / ((double) width) * ((double) minecraft.getWindow().getScreenWidth());
        return (int) sx;
    }

    public static void registerKeyInput() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.cinemamod.openrequestbrowser",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.cinemamod.keybinds"
        ));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (keyBinding.consumeClick()) {
                client.setScreen(new VideoRequestBrowser());
            }
        });
    }

    public static void registerCefTick() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (browser != null) {
                browser.update();
            }
        });
    }

}
