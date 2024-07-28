package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.gui.interfaces.ISliderCallback;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class WidgetSlider extends WidgetBase {
    public static final Identifier BUTTON_TEXTURE = new Identifier("widget/button");
    public static final Identifier BUTTON_DISABLE_TEXTURE = new Identifier("widget/button_disabled");
    protected static final Identifier WIDGET_TEXTURE = new Identifier("textures/gui/widgets.png");
    protected final ISliderCallback callback;
    protected int sliderWidth;
    protected int lastMouseX;
    protected boolean dragging;

    public WidgetSlider(int x, int y, int width, int height, ISliderCallback callback) {
        super(x, y, width, height);

        this.callback = callback;
        int usableWidth = this.width - 4;
        this.sliderWidth = MathHelper.clamp(usableWidth / callback.getMaxSteps(), 8, usableWidth / 2);
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        this.callback.setValueRelative(this.getRelativePosition(mouseX));
        this.lastMouseX = mouseX;
        this.dragging = true;

        return true;
    }

    @Override
    public void onMouseReleasedImpl(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        if (this.dragging && mouseX != this.lastMouseX) {
            this.callback.setValueRelative(this.getRelativePosition(mouseX));
            this.lastMouseX = mouseX;
        }

        RenderUtils.color(1f, 1f, 1f, 1f);

        drawContext.drawNineSlicedTexture(WIDGET_TEXTURE, this.x + 1, this.y, this.width - 3, this.height, 20, 4, 200, 20, 0, 46);
//        drawContext.drawTexture(WidgetSlider.BUTTON_DISABLE_TEXTURE, this.x + 1, this.y, 0, 0, this.width - 3, 20);

        double relPos = this.callback.getValueRelative();
        int sw = this.sliderWidth;
        int usableWidth = this.width - 4 - sw;
        int s = sw / 2;

        drawContext.drawNineSlicedTexture(WIDGET_TEXTURE, this.x + 2 + (int) (relPos * usableWidth), this.y, sw, 20, 20, 4, 200, 20, 0, 66);
        //drawContext.drawTexture(WidgetSlider.BUTTON_TEXTURE, this.x + 2 + (int) (relPos * usableWidth), 0, 0, this.y, sw, 20);

        String str = this.callback.getFormattedDisplayValue();
        int w = this.getStringWidth(str);
        this.drawString(this.x + (this.width / 2) - w / 2, this.y + 6, 0xFFFFFFA0, str, drawContext);
    }

    protected double getRelativePosition(int mouseX) {
        int relPos = mouseX - this.x - this.sliderWidth / 2;
        return MathHelper.clamp((double) relPos / (double) (this.width - this.sliderWidth - 4), 0, 1);
    }
}
