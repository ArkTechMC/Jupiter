package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.gui.GuiBase;
import com.iafenvoy.jupiter.malilib.gui.GuiTextFieldGeneric;
import com.iafenvoy.jupiter.malilib.gui.LeftRight;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IGuiIcon;
import com.iafenvoy.jupiter.malilib.util.KeyCodes;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;

public class WidgetSearchBar extends WidgetBase {
    protected final WidgetIcon iconSearch;
    protected final LeftRight iconAlignment;
    protected final GuiTextFieldGeneric searchBox;
    protected boolean searchOpen;

    public WidgetSearchBar(int x, int y, int width, int height,
                           int searchBarOffsetX, IGuiIcon iconSearch, LeftRight iconAlignment) {
        super(x, y, width, height);

        int iw = iconSearch.getWidth();
        int ix = iconAlignment == LeftRight.RIGHT ? x + width - iw - 1 : x + 2;
        int tx = iconAlignment == LeftRight.RIGHT ? x - searchBarOffsetX + 1 : x + iw + 6 + searchBarOffsetX;
        this.iconSearch = new WidgetIcon(ix, y + 1, iconSearch);
        this.iconAlignment = iconAlignment;
        this.searchBox = new GuiTextFieldGeneric(tx, y, width - iw - 7 - Math.abs(searchBarOffsetX), height, this.textRenderer);
        this.searchBox.setZLevel(this.zLevel);
    }

    public String getFilter() {
        return this.searchOpen ? this.searchBox.getText().toLowerCase() : "";
    }

    public boolean hasFilter() {
        return this.searchOpen && !this.searchBox.getText().isEmpty();
    }

    public boolean isSearchOpen() {
        return this.searchOpen;
    }

    public void setSearchOpen(boolean isOpen) {
        this.searchOpen = isOpen;

        if (this.searchOpen) {
            this.searchBox.setFocused(true);
        }
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (this.searchOpen && this.searchBox.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        } else if (this.iconSearch.isMouseOver(mouseX, mouseY)) {
            this.setSearchOpen(!this.searchOpen);
            return true;
        }

        return false;
    }

    @Override
    protected boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        if (this.searchOpen) {
            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            } else if (keyCode == KeyCodes.KEY_ESCAPE) {
                if (GuiBase.isShiftDown()) {
                    this.mc.currentScreen.close();
                }

                this.searchOpen = false;
                this.searchBox.setFocused(false);
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        if (this.searchOpen) {
            return this.searchBox.charTyped(charIn, modifiers);
        } else if (SharedConstants.isValidChar(charIn)) {
            this.searchOpen = true;
            this.searchBox.setFocused(true);
            this.searchBox.setText("");
            this.searchBox.charTyped(charIn, modifiers);

            return true;
        }

        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        RenderUtils.color(1f, 1f, 1f, 1f);
        this.iconSearch.render(false, this.iconSearch.isMouseOver(mouseX, mouseY));

        if (this.searchOpen) {
            this.searchBox.render(drawContext, mouseX, mouseY, 0);
        }
    }
}
