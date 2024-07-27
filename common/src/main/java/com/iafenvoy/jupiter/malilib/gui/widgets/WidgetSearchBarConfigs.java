package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.gui.LeftRight;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IGuiIcon;

public class WidgetSearchBarConfigs extends WidgetSearchBar {
    public WidgetSearchBarConfigs(int x, int y, int width, int height, int searchBarOffsetX,
                                  IGuiIcon iconSearch, LeftRight iconAlignment) {
        super(x, y + 3, width - 160, 14, searchBarOffsetX, iconSearch, iconAlignment);
    }

    @Override
    public boolean hasFilter() {
        return super.hasFilter() || this.searchOpen;
    }
}
