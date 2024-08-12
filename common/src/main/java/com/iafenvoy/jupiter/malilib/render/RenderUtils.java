package com.iafenvoy.jupiter.malilib.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

public class RenderUtils {
    public static void setupBlend() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
    }

    public static void setupBlendSimple() {
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void bindTexture(Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
    }

    public static void color(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }

    public static void disableDiffuseLighting() {
        // FIXME 1.15-pre4+
        DiffuseLighting.disableGuiDepthLighting();
    }

    public static void enableDiffuseLightingForLevel(MatrixStack matrixStack) {
        DiffuseLighting.enableForLevel(matrixStack.peek().getPositionMatrix());
    }

    public static void enableDiffuseLightingGui3D() {
        // FIXME 1.15-pre4+
        DiffuseLighting.enableGuiDepthLighting();
    }

    public static void drawOutlinedBox(int x, int y, int width, int height, int colorBg, int colorBorder) {
        drawOutlinedBox(x, y, width, height, colorBg, colorBorder, 0f);
    }

    public static void drawOutlinedBox(int x, int y, int width, int height, int colorBg, int colorBorder, float zLevel) {
        // Draw the background
        drawRect(x, y, width, height, colorBg, zLevel);

        // Draw the border
        drawOutline(x - 1, y - 1, width + 2, height + 2, colorBorder, zLevel);
    }

    public static void drawOutline(int x, int y, int width, int height, int colorBorder) {
        drawOutline(x, y, width, height, colorBorder, 0f);
    }

    public static void drawOutline(int x, int y, int width, int height, int colorBorder, float zLevel) {
        drawRect(x, y, 1, height, colorBorder, zLevel); // left edge
        drawRect(x + width - 1, y, 1, height, colorBorder, zLevel); // right edge
        drawRect(x + 1, y, width - 2, 1, colorBorder, zLevel); // top edge
        drawRect(x + 1, y + height - 1, width - 2, 1, colorBorder, zLevel); // bottom edge
    }

    public static void drawOutline(int x, int y, int width, int height, int borderWidth, int colorBorder) {
        drawOutline(x, y, width, height, borderWidth, colorBorder, 0f);
    }

    public static void drawOutline(int x, int y, int width, int height, int borderWidth, int colorBorder, float zLevel) {
        drawRect(x, y, borderWidth, height, colorBorder, zLevel); // left edge
        drawRect(x + width - borderWidth, y, borderWidth, height, colorBorder, zLevel); // right edge
        drawRect(x + borderWidth, y, width - 2 * borderWidth, borderWidth, colorBorder, zLevel); // top edge
        drawRect(x + borderWidth, y + height - borderWidth, width - 2 * borderWidth, borderWidth, colorBorder, zLevel); // bottom edge
    }

    public static void drawTexturedRect(int x, int y, int u, int v, int width, int height) {
        drawTexturedRect(x, y, u, v, width, height, 0);
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        drawRect(x, y, width, height, color, 0f);
    }

    public static void drawRect(int x, int y, int width, int height, int color, float zLevel) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.applyModelViewMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        setupBlend();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(x, y, zLevel).color(r, g, b, a).next();
        buffer.vertex(x, y + height, zLevel).color(r, g, b, a).next();
        buffer.vertex(x + width, y + height, zLevel).color(r, g, b, a).next();
        buffer.vertex(x + width, y, zLevel).color(r, g, b, a).next();

        tessellator.draw();

        RenderSystem.disableBlend();
    }

    public static void drawTexturedRect(int x, int y, int u, int v, int width, int height, float zLevel) {
        float pixelWidth = 0.00390625F;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.applyModelViewMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        setupBlend();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        buffer.vertex(x, y + height, zLevel).texture(u * pixelWidth, (v + height) * pixelWidth).next();
        buffer.vertex(x + width, y + height, zLevel).texture((u + width) * pixelWidth, (v + height) * pixelWidth).next();
        buffer.vertex(x + width, y, zLevel).texture((u + width) * pixelWidth, v * pixelWidth).next();
        buffer.vertex(x, y, zLevel).texture(u * pixelWidth, v * pixelWidth).next();

        tessellator.draw();
    }

    public static void drawTexturedRectBatched(int x, int y, int u, int v, int width, int height, BufferBuilder buffer) {
        drawTexturedRectBatched(x, y, u, v, width, height, 0, buffer);
    }

    public static void drawTexturedRectBatched(int x, int y, int u, int v, int width, int height, float zLevel, BufferBuilder buffer) {
        float pixelWidth = 0.00390625F;

        buffer.vertex(x, y + height, zLevel).texture(u * pixelWidth, (v + height) * pixelWidth).next();
        buffer.vertex(x + width, y + height, zLevel).texture((u + width) * pixelWidth, (v + height) * pixelWidth).next();
        buffer.vertex(x + width, y, zLevel).texture((u + width) * pixelWidth, v * pixelWidth).next();
        buffer.vertex(x, y, zLevel).texture(u * pixelWidth, v * pixelWidth).next();
    }

    public static void drawHoverText(int x, int y, List<String> textLines, DrawContext drawContext) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (!textLines.isEmpty() && MinecraftClient.getInstance().currentScreen != null) {
            RenderSystem.enableDepthTest();
            TextRenderer font = mc.textRenderer;
            int maxLineLength = 0;
            int maxWidth = MinecraftClient.getInstance().currentScreen.width;
            List<String> linesNew = new ArrayList<>();

            for (String lineOrig : textLines) {
                String[] lines = lineOrig.split("\\n");

                for (String line : lines) {
                    int length = font.getWidth(line);

                    if (length > maxLineLength) {
                        maxLineLength = length;
                    }

                    linesNew.add(line);
                }
            }

            textLines = linesNew;

            final int lineHeight = font.fontHeight + 1;
            int textHeight = textLines.size() * lineHeight - 2;
            int textStartX = x + 4;
            int textStartY = Math.max(8, y - textHeight - 6);

            if (textStartX + maxLineLength + 6 > maxWidth) {
                textStartX = Math.max(2, maxWidth - maxLineLength - 8);
            }

            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            matrixStack.translate(0, 0, 300);
            RenderSystem.applyModelViewMatrix();

            double zLevel = 300;
            int borderColor = 0xF0100010;
            drawGradientRect(textStartX - 3, textStartY - 4, textStartX + maxLineLength + 3, textStartY - 3, zLevel, borderColor, borderColor);
            drawGradientRect(textStartX - 3, textStartY + textHeight + 3, textStartX + maxLineLength + 3, textStartY + textHeight + 4, zLevel, borderColor, borderColor);
            drawGradientRect(textStartX - 3, textStartY - 3, textStartX + maxLineLength + 3, textStartY + textHeight + 3, zLevel, borderColor, borderColor);
            drawGradientRect(textStartX - 4, textStartY - 3, textStartX - 3, textStartY + textHeight + 3, zLevel, borderColor, borderColor);
            drawGradientRect(textStartX + maxLineLength + 3, textStartY - 3, textStartX + maxLineLength + 4, textStartY + textHeight + 3, zLevel, borderColor, borderColor);

            int fillColor1 = 0x505000FF;
            int fillColor2 = 0x5028007F;
            drawGradientRect(textStartX - 3, textStartY - 3 + 1, textStartX - 3 + 1, textStartY + textHeight + 3 - 1, zLevel, fillColor1, fillColor2);
            drawGradientRect(textStartX + maxLineLength + 2, textStartY - 3 + 1, textStartX + maxLineLength + 3, textStartY + textHeight + 3 - 1, zLevel, fillColor1, fillColor2);
            drawGradientRect(textStartX - 3, textStartY - 3, textStartX + maxLineLength + 3, textStartY - 3 + 1, zLevel, fillColor1, fillColor1);
            drawGradientRect(textStartX - 3, textStartY + textHeight + 2, textStartX + maxLineLength + 3, textStartY + textHeight + 3, zLevel, fillColor2, fillColor2);

            for (String str : textLines) {
                drawContext.drawText(font, str, textStartX, textStartY, 0xFFFFFFFF, false);
                textStartY += lineHeight;
            }

            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
        }
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, double zLevel, int startColor, int endColor) {
        int sa = (startColor >> 24 & 0xFF);
        int sr = (startColor >> 16 & 0xFF);
        int sg = (startColor >> 8 & 0xFF);
        int sb = (startColor & 0xFF);

        int ea = (endColor >> 24 & 0xFF);
        int er = (endColor >> 16 & 0xFF);
        int eg = (endColor >> 8 & 0xFF);
        int eb = (endColor & 0xFF);

        setupBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.applyModelViewMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(right, top, zLevel).color(sr, sg, sb, sa).next();
        buffer.vertex(left, top, zLevel).color(sr, sg, sb, sa).next();
        buffer.vertex(left, bottom, zLevel).color(er, eg, eb, ea).next();
        buffer.vertex(right, bottom, zLevel).color(er, eg, eb, ea).next();

        tessellator.draw();

        RenderSystem.disableBlend();
    }

    public static void drawCenteredString(int x, int y, int color, String text, DrawContext drawContext) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        drawContext.drawCenteredTextWithShadow(textRenderer, text, x, y, color);
    }

    public static void drawHorizontalLine(int x, int y, int width, int color) {
        drawRect(x, y, width, 1, color);
    }

    public static void drawVerticalLine(int x, int y, int height, int color) {
        drawRect(x, y, 1, height, color);
    }

    public static void renderSprite(int x, int y, int width, int height, Identifier atlas, Identifier texture, DrawContext drawContext) {
        if (texture != null) {
            Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(atlas).apply(texture);
            drawContext.drawSprite(x, y, 0, width, height, sprite);
        }
    }

    public static void renderText(int x, int y, int color, String text, DrawContext drawContext) {
        String[] parts = text.split("\\\\n");
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        for (String line : parts) {
            drawContext.drawText(textRenderer, line, x, y, color, true);
            y += textRenderer.fontHeight + 1;
        }
    }

    public static void renderText(int x, int y, int color, List<String> lines, DrawContext drawContext) {
        if (!lines.isEmpty()) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

            for (String line : lines) {
                drawContext.drawText(textRenderer, line, x, y, color, false);
                y += textRenderer.fontHeight + 2;
            }
        }
    }

    /**
     * Renders a text plate/billboard, similar to the player name plate.<br>
     * The plate will always face towards the viewer.
     */
    public static void drawTextPlate(List<String> text, double x, double y, double z, float scale) {
        Entity entity = MinecraftClient.getInstance().getCameraEntity();

        if (entity != null) {
            drawTextPlate(text, x, y, z, entity.getYaw(), entity.getPitch(), scale, 0xFFFFFFFF, 0x40000000, true);
        }
    }

    public static void drawTextPlate(List<String> text, double x, double y, double z, float yaw, float pitch,
                                     float scale, int textColor, int bgColor, boolean disableDepth) {
        Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        double cx = cameraPos.x;
        double cy = cameraPos.y;
        double cz = cameraPos.z;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        MatrixStack globalStack = RenderSystem.getModelViewStack();
        globalStack.push();
        globalStack.translate(x - cx, y - cy, z - cz);

        Quaternionf rot = new Quaternionf().rotationYXZ(-yaw * (float) (Math.PI / 180.0), pitch * (float) (Math.PI / 180.0), 0.0F);
        globalStack.multiply(rot);

        globalStack.scale(-scale, -scale, scale);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.disableCull();

        setupBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int maxLineLen = 0;

        for (String line : text) {
            maxLineLen = Math.max(maxLineLen, textRenderer.getWidth(line));
        }

        int strLenHalf = maxLineLen / 2;
        int textHeight = textRenderer.fontHeight * text.size() - 1;
        int bga = ((bgColor >>> 24) & 0xFF);
        int bgr = ((bgColor >>> 16) & 0xFF);
        int bgg = ((bgColor >>> 8) & 0xFF);
        int bgb = (bgColor & 0xFF);

        if (disableDepth) {
            RenderSystem.depthMask(false);
            RenderSystem.disableDepthTest();
        }

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(-strLenHalf - 1, -1, 0.0D).color(bgr, bgg, bgb, bga).next();
        buffer.vertex(-strLenHalf - 1, textHeight, 0.0D).color(bgr, bgg, bgb, bga).next();
        buffer.vertex(strLenHalf, textHeight, 0.0D).color(bgr, bgg, bgb, bga).next();
        buffer.vertex(strLenHalf, -1, 0.0D).color(bgr, bgg, bgb, bga).next();
        tessellator.draw();

        int textY = 0;

        // translate the text a bit infront of the background
        if (!disableDepth) {
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(-0.6f, -1.2f);
        }

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity();

        for (String line : text) {
            if (disableDepth) {
                RenderSystem.depthMask(false);
                RenderSystem.disableDepthTest();
                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(buffer);
                textRenderer.draw(line, -strLenHalf, textY, 0x20000000 | (textColor & 0xFFFFFF), false, modelMatrix, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
                immediate.draw();
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(true);
            }

            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(buffer);
            textRenderer.draw(line, -strLenHalf, textY, textColor, false, modelMatrix, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
            immediate.draw();
            textY += textRenderer.fontHeight;
        }

        if (!disableDepth) {
            RenderSystem.polygonOffset(0f, 0f);
            RenderSystem.disablePolygonOffset();
        }

        color(1f, 1f, 1f, 1f);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        globalStack.pop();
    }

    public static void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d, float zLevel) {
        setupGuiTransform(RenderSystem.getModelViewStack(), xPosition, yPosition, zLevel);
    }

    public static void setupGuiTransform(MatrixStack matrixStack, int xPosition, int yPosition, float zLevel) {
        matrixStack.translate(xPosition + 8.0, yPosition + 8.0, zLevel + 100.0);
        matrixStack.scale(16, -16, 16);
    }
}
