package com.cinemamod.fabric.block.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.client.renderer.GameRenderer;

public final class RenderUtil {

    public static void fixRotation(PoseStack matrixStack, String facing) {
        final Quaternion rotation;

        switch (facing) {
            case "NORTH":
                rotation = new Quaternion(0, 180, 0, true);
                matrixStack.translate(0, 0, 1);
                break;
            case "WEST":
                rotation = new Quaternion(0, -90, 0, true);
                matrixStack.translate(0, 0, 0);
                break;
            case "EAST":
                rotation = new Quaternion(0, 90, 0, true);
                matrixStack.translate(-1, 0, 1);
                break;
            default:
                rotation = new Quaternion(0, 0, 0, true);
                matrixStack.translate(-1, 0, 0);
                break;
        }

        matrixStack.mulPose(rotation);
    }

    public static void moveForward(PoseStack matrixStack, String facing, float amount) {
        switch (facing) {
            case "NORTH":
                matrixStack.translate(0, 0, -amount);
                break;
            case "WEST":
                matrixStack.translate(-amount, 0, 0);
                break;
            case "EAST":
                matrixStack.translate(amount, 0, 0);
                break;
            default:
                matrixStack.translate(0, 0, amount);
                break;
        }
    }

    public static void moveHorizontal(PoseStack matrixStack, String facing, float amount) {
        switch (facing) {
            case "NORTH":
                matrixStack.translate(-amount, 0, 0);
                break;
            case "WEST":
                matrixStack.translate(0, 0, amount);
                break;
            case "EAST":
                matrixStack.translate(0, 0, -amount);
                break;
            default:
                matrixStack.translate(amount, 0, 0);
                break;
        }
    }

    public static void moveVertical(PoseStack matrixStack, float amount) {
        matrixStack.translate(0, amount, 0);
    }

    public static void renderTexture(PoseStack matrixStack, Tesselator tessellator, BufferBuilder buffer, int glId) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, glId);
        Matrix4f matrix4f = matrixStack.last().pose();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(255, 255, 255, 255).uv(0.0f, 1.0f).endVertex();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(255, 255, 255, 255).uv(1.0f, 1.0f).endVertex();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(1.0f, 0.0f).endVertex();
        buffer.vertex(matrix4f, 0, 0, 0).color(255, 255, 255, 255).uv(0.0f, 0.0f).endVertex();
        tessellator.end();
        RenderSystem.setShaderTexture(0, 0);
    }

    public static void renderColor(PoseStack matrixStack, Tesselator tessellator, BufferBuilder buffer, int r, int g, int b) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Matrix4f matrix4f = matrixStack.last().pose();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix4f, 0.0F, -1.0F, 1.0F).color(r, g, b, 255).endVertex();
        buffer.vertex(matrix4f, 1.0F, -1.0F, 1.0F).color(r, g, b, 255).endVertex();
        buffer.vertex(matrix4f, 1.0F, 0.0F, 0.0F).color(r, g, b, 255).endVertex();
        buffer.vertex(matrix4f, 0, 0, 0).color(r, g, b, 255).endVertex();
        tessellator.end();
    }

    public static void renderBlack(PoseStack matrixStack, Tesselator tessellator, BufferBuilder buffer) {
        renderColor(matrixStack, tessellator, buffer, 0, 0, 0);
    }

}
