package com.cinemamod.fabric.block.render;

import com.cinemamod.fabric.CinemaModClient;
import com.cinemamod.fabric.block.ScreenBlockEntity;
import com.cinemamod.fabric.screen.Screen;
import com.cinemamod.fabric.screen.ScreenManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ScreenBlockEntityRenderer implements BlockEntityRenderer<ScreenBlockEntity> {

    public ScreenBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(ScreenBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ScreenManager screenManager = CinemaModClient.getInstance().getScreenManager();
        Screen screen = screenManager.getScreen(entity.getBlockPos());
        if (screen == null || !screen.isVisible()) return;
        RenderSystem.enableDepthTest();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        renderScreenTexture(screen, matrices, tessellator, buffer);
        RenderSystem.disableDepthTest();
    }

    private static void renderScreenTexture(Screen screen, PoseStack matrices, Tesselator tessellator, BufferBuilder buffer) {
        matrices.pushPose();
        matrices.translate(1, 1, 0);
        RenderUtil.moveForward(matrices, screen.getFacing(), 0.008f);
        RenderUtil.fixRotation(matrices, screen.getFacing());
        matrices.scale(screen.getWidth(), screen.getHeight(), 0);
        if (screen.hasBrowser()) {
            int glId = screen.getBrowser().renderer_.texture_id_[0];
            RenderUtil.renderTexture(matrices, tessellator, buffer, glId);
        } else {
            RenderUtil.renderBlack(matrices, tessellator, buffer);
        }
        matrices.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ScreenBlockEntity blockEntity) {
        return true;
    }

    public static void register() {
        BlockEntityRendererRegistry.register(ScreenBlockEntity.SCREEN_BLOCK_ENTITY, ScreenBlockEntityRenderer::new);
    }

}
