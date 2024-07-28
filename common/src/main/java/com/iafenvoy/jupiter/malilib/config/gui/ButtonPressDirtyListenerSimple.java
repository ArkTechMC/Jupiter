package com.iafenvoy.jupiter.malilib.config.gui;

import com.iafenvoy.jupiter.malilib.gui.button.ButtonBase;
import com.iafenvoy.jupiter.malilib.gui.button.IButtonActionListener;

public class ButtonPressDirtyListenerSimple implements IButtonActionListener {
    private boolean dirty;

    @Override
    public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void resetDirty() {
        this.dirty = false;
    }
}
