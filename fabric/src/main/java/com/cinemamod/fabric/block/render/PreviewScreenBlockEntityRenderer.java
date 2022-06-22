package com.cinemamod.fabric.block.render;

import com.cinemamod.common.CinemaModCommonClient;
import com.cinemamod.fabric.block.PreviewScreenBlockEntity;
import com.cinemamod.fabric.screen.PreviewScreen;
import com.cinemamod.common.screen.PreviewScreenManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Quaternion;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class PreviewScreenBlockEntityRenderer implements BlockEntityRenderer<PreviewScreenBlockEntity> {

    public PreviewScreenBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(PreviewScreenBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        PreviewScreenManager previewScreenManager = CinemaModCommonClient.getInstance().getPreviewScreenManager();
        PreviewScreen previewScreen = (PreviewScreen) previewScreenManager.getPreviewScreen(entity.getBlockPos());
        if (previewScreen == null) return;
        RenderSystem.enableDepthTest();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        renderScreenTexture(previewScreen, matrices, tessellator, buffer);
        renderVideoThumbnail(previewScreen, matrices, tessellator, buffer);
        renderScreenText(previewScreen, matrices);
        RenderSystem.disableDepthTest();
    }

    private static void renderScreenTexture(PreviewScreen previewScreen, PoseStack matrices, Tesselator tessellator, BufferBuilder buffer) {
        DynamicTexture texture = previewScreen.hasVideoInfo() ? previewScreen.getActiveTexture() : previewScreen.getStaticTexture();

        if (texture != null) {
            matrices.pushPose();
            matrices.translate(1, 1, 0);
            RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.008f);
            RenderUtil.fixRotation(matrices, previewScreen.getFacing());
            matrices.scale(3, 2, 0);
            RenderUtil.renderTexture(matrices, tessellator, buffer, texture.getId());
            matrices.popPose();
        }
    }

    private static void renderVideoThumbnail(PreviewScreen previewScreen, PoseStack matrices, Tesselator tessellator, BufferBuilder buffer) {
        DynamicTexture texture = previewScreen.getThumbnailTexture();

        if (texture != null) {
            matrices.pushPose();
            matrices.translate(1, 1, 0);
            RenderUtil.moveHorizontal(matrices, previewScreen.getFacing(), 0.5f);
            RenderUtil.moveVertical(matrices, -1 / 3f);
            RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.01f);
            RenderUtil.fixRotation(matrices, previewScreen.getFacing());
            matrices.scale(3 / 1.5f, 2 / 1.5f, 0);
            RenderUtil.renderTexture(matrices, tessellator, buffer, texture.getId());
            matrices.popPose();
        }
    }

    private static void renderScreenText(PreviewScreen previewScreen, PoseStack matrices) {
        matrices.pushPose();
        matrices.translate(1, 1, 0);
        RenderUtil.moveHorizontal(matrices, previewScreen.getFacing(), 0.1f);
        RenderUtil.moveVertical(matrices, -0.15f);
        RenderUtil.moveForward(matrices, previewScreen.getFacing(), 0.01f);
        RenderUtil.fixRotation(matrices, previewScreen.getFacing());
        matrices.mulPose(new Quaternion(180, 0, 0, true));
        matrices.scale(0.02f, 0.02f, 0.02f);
        Font textRenderer = Minecraft.getInstance().font;
        final String topText;
        final String bottomText;
        if (previewScreen.hasVideoInfo()) {
            topText = previewScreen.getVideoInfo().getTitleShort();
            bottomText = previewScreen.getVideoInfo().getPoster();
        } else {
            topText = "NOTHING PLAYING";
            bottomText = "";
        }
        textRenderer.draw(matrices, topText, 0F, 0F, 16777215);
        RenderUtil.moveVertical(matrices, 78f);
        textRenderer.draw(matrices, bottomText, 0F, 0F, 16777215);
        matrices.popPose();
    }

    public static void register() {
        BlockEntityRendererRegistry.register(PreviewScreenBlockEntity.PREVIEW_SCREEN_BLOCK_ENTITY, PreviewScreenBlockEntityRenderer::new);
    }

}
