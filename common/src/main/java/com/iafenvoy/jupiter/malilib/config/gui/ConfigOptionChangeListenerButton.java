package com.iafenvoy.jupiter.malilib.config.gui;

import com.iafenvoy.jupiter.malilib.config.IConfigResettable;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonBase;
import com.iafenvoy.jupiter.malilib.gui.button.IButtonActionListener;
import org.jetbrains.annotations.Nullable;

public class ConfigOptionChangeListenerButton implements IButtonActionListener {
    private final IConfigResettable config;
    private final ButtonBase buttonReset;
    @Nullable
    private final ButtonPressDirtyListenerSimple dirtyListener;

    public ConfigOptionChangeListenerButton(IConfigResettable config, ButtonBase buttonReset, @Nullable ButtonPressDirtyListenerSimple dirtyListener) {
        this.config = config;
        this.dirtyListener = dirtyListener;
        this.buttonReset = buttonReset;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
        this.buttonReset.setEnabled(this.config.isModified());

        if (this.dirtyListener != null) {
            // Call the dirty listener to know if the configs should be saved when the GUI is closed
            this.dirtyListener.actionPerformedWithButton(button, mouseButton);
        }
    }
}
