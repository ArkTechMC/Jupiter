package com.iafenvoy.jupiter.malilib.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class MalilibDrawContext extends DrawContext {
    public MalilibDrawContext(MinecraftClient client, Immediate vertexConsumers) {
        super(client, vertexConsumers);
    }

    @Override
    public void draw() {
        // Omit the disableDepthTest() call >_>
        this.getVertexConsumers().draw();
    }

    @Override
    public int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow) {
        if (text == null) {
            return 0;
        } else {
            return textRenderer.draw(text, (float)x, (float)y, color, false, this.getMatrices().peek().getPositionMatrix(), this.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880, textRenderer.isRightToLeft());
        }
    }

    @Override
    public int drawText(TextRenderer textRenderer, OrderedText text, int x, int y, int color, boolean shadow) {
        return textRenderer.draw(text, (float)x, (float)y, color, false, this.getMatrices().peek().getPositionMatrix(), this.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
    }
}
