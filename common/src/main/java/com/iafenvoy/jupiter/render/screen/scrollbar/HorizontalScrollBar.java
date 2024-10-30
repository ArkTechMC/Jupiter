package com.iafenvoy.jupiter.render.screen.scrollbar;

import com.iafenvoy.jupiter.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class HorizontalScrollBar {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    protected boolean mouseOver = false;
    protected boolean dragging = false;
    protected boolean renderScrollbarBackground = true;
    protected int currentValue = 0;
    protected int maxValue = 100;
    protected int backgroundColor = 0x44FFFFFF;
    protected int foregroundColor = 0xFFFFFFFF;
    protected int dragStartValue = 0;
    protected int dragStartX = 0;

    public HorizontalScrollBar setRenderBarBackground(boolean render) {
        this.renderScrollbarBackground = render;
        return this;
    }

    public int getValue() {
        return this.currentValue;
    }

    public void setValue(int value) {
        this.currentValue = MathHelper.clamp(value, 0, this.maxValue);
    }

    public void offsetValue(int offset) {
        this.setValue(this.currentValue + offset);
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = Math.max(0, maxValue);
        this.currentValue = Math.min(this.currentValue, this.maxValue);
    }

    public boolean wasMouseOver() {
        return this.mouseOver;
    }

    public void setIsDragging(boolean isDragging) {
        this.dragging = isDragging;
    }

    public void render(int mouseX, int mouseY, float partialTicks, int xPosition, int yPosition, int width, int height, int totalWidth) {
        if (this.renderScrollbarBackground) {
            RenderUtils.drawRect(xPosition, yPosition, width, height, this.backgroundColor);
        }

        if (totalWidth > 0) {
            int slideWidth = width - 2;
            float relative = Math.min(1.0F, (float) slideWidth / (float) totalWidth);
            int barWidth = (int) (relative * slideWidth);
            int barTravel = slideWidth - barWidth;
            int barPosition = xPosition + 1 + (this.maxValue > 0 ? (int) ((this.currentValue / (float) this.maxValue) * barTravel) : 0);
            RenderUtils.drawRect(barPosition, yPosition + 1, barWidth, height - 2, this.foregroundColor);
            this.mouseOver = mouseY > yPosition && mouseY < yPosition + height && mouseX > barPosition && mouseX < barPosition + barWidth;
            this.handleDrag(mouseX, barTravel);
        }
    }

    public void handleDrag(int mouseX, int barTravel) {
        if (this.dragging) {
            float valuePerPixel = (float) this.maxValue / barTravel;
            this.setValue((int) (this.dragStartValue + ((mouseX - this.dragStartX) * valuePerPixel)));
        } else {
            this.dragStartX = mouseX;
            this.dragStartValue = this.currentValue;
        }
    }

    public boolean isDragging() {
        return dragging;
    }
}
