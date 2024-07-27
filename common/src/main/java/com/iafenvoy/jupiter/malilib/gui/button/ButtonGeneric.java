package com.iafenvoy.jupiter.malilib.gui.button;

import com.iafenvoy.jupiter.malilib.gui.LeftRight;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IGuiIcon;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class ButtonGeneric extends ButtonBase {
    @Nullable
    protected final IGuiIcon icon;
    protected LeftRight alignment = LeftRight.LEFT;
    protected boolean textCentered;
    protected boolean renderDefaultBackground = true;

    public ButtonGeneric(int x, int y, int width, boolean rightAlign, String translationKey, Object... args) {
        this(x, y, width, 20, com.iafenvoy.jupiter.malilib.util.StringUtils.translate(translationKey, args));

        if (rightAlign) {
            this.x = x - this.width;
        }
    }

    public ButtonGeneric(int x, int y, int width, int height, String text, String... hoverStrings) {
        this(x, y, width, height, text, null, hoverStrings);

        this.textCentered = true;
    }

    public ButtonGeneric(int x, int y, int width, int height, String text, IGuiIcon icon, String... hoverStrings) {
        super(x, y, width, height, text);

        this.icon = icon;

        if (width == -1 && icon != null) {
            this.width += icon.getWidth() + 8;
        }

        if (hoverStrings.length > 0) {
            this.setHoverStrings(hoverStrings);
        }
    }

    public ButtonGeneric(int x, int y, IGuiIcon icon, String... hoverStrings) {
        this(x, y, icon.getWidth(), icon.getHeight(), "", icon, hoverStrings);

        this.setRenderDefaultBackground(false);
    }

    @Override
    public ButtonGeneric setActionListener(@Nullable IButtonActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public ButtonGeneric setTextCentered(boolean centered) {
        this.textCentered = centered;
        return this;
    }

    /**
     * Set the icon aligment.<br>
     * Note: Only LEFT and RIGHT alignments work properly.
     *
     * @param alignment
     * @return
     */
    public ButtonGeneric setIconAlignment(LeftRight alignment) {
        this.alignment = alignment;
        return this;
    }

    public ButtonGeneric setRenderDefaultBackground(boolean render) {
        this.renderDefaultBackground = render;
        return this;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            RenderUtils.color(1f, 1f, 1f, 1f);

            if (this.renderDefaultBackground) {
                int v = !this.enabled ? 46 : this.hovered ? 86 : 66;
                drawContext.drawNineSlicedTexture(WIDGET_TEXTURE, this.x, this.y, this.width, this.height, 20, 4, 200, 20, 0, v);
            }

            if (this.icon != null) {
                int offset = this.renderDefaultBackground ? 4 : 0;
                int x = this.alignment == LeftRight.LEFT ? this.x + offset : this.x + this.width - this.icon.getWidth() - offset;
                int y = this.y + (this.height - this.icon.getHeight()) / 2;
                int u = this.icon.getU() + this.getTextureOffset(this.hovered) * this.icon.getWidth(); // FIXME: What happened here.

                this.bindTexture(this.icon.getTexture());
                RenderUtils.drawTexturedRect(x, y, u, this.icon.getV(), this.icon.getWidth(), this.icon.getHeight());
            }

            if (!StringUtils.isBlank(this.displayString)) {
                int y = this.y + (this.height - 8) / 2;
                int color = 0xE0E0E0;

                if (!this.enabled) {
                    color = 0xA0A0A0;
                } else if (this.hovered) {
                    color = 0xFFFFA0;
                }

                if (this.textCentered) {
                    this.drawCenteredStringWithShadow(this.x + this.width / 2, y, color, this.displayString, drawContext);
                } else {
                    int x = this.x + 6;

                    if (this.icon != null && this.alignment == LeftRight.LEFT) {
                        x += this.icon.getWidth() + 2;
                    }

                    this.drawStringWithShadow(x, y, color, this.displayString, drawContext);
                }
            }
        }
    }
}
