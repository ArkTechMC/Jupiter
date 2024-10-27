package com.iafenvoy.jupiter.render.screen.scrollbar;

import com.iafenvoy.jupiter.malilib.gui.interfaces.IGuiIcon;
import com.iafenvoy.jupiter.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class HorizontalScrollBar {
    @Nullable
    protected final IGuiIcon barTexture;
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

    public HorizontalScrollBar() {
        this(null);
    }

    public HorizontalScrollBar(@Nullable IGuiIcon barTexture) {
        this.barTexture = barTexture;
    }

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

            if (this.barTexture != null && barWidth >= 4) {
                RenderUtils.color(1f, 1f, 1f, 1f);
                RenderUtils.bindTexture(this.barTexture.getTexture());
                int u = this.barTexture.getU();
                int v = this.barTexture.getV();
                int w = this.barTexture.getWidth();
                int h = this.barTexture.getHeight();

                RenderUtils.drawTexturedRect(barPosition, yPosition + 1, u, v, barWidth - 2, h);
                RenderUtils.drawTexturedRect(barPosition + barWidth - 2, yPosition + 1, u, v + h - 2, 2, h);
            } else {
                RenderUtils.drawRect(barPosition, yPosition + 1, barWidth, height - 2, this.foregroundColor);
            }

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
}
