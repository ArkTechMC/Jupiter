package com.iafenvoy.jupiter.malilib.gui.widgets;

import org.jetbrains.annotations.Nullable;

public class WidgetListEntryBase<TYPE> extends WidgetContainer {
    @Nullable
    protected final TYPE entry;
    protected final int listIndex;

    public WidgetListEntryBase(int x, int y, int width, int height, @Nullable TYPE entry, int listIndex) {
        super(x, y, width, height);

        this.entry = entry;
        this.listIndex = listIndex;
    }

    @Nullable
    public TYPE getEntry() {
        return this.entry;
    }

    public int getListIndex() {
        return this.listIndex;
    }
}
