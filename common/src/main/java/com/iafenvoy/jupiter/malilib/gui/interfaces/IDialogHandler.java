package com.iafenvoy.jupiter.malilib.gui.interfaces;

import com.iafenvoy.jupiter.malilib.gui.GuiBase;

public interface IDialogHandler {
    /**
     * Open the provided GUI as a "dialog window"
     *
     */
    void openDialog(GuiBase gui);

    /**
     * Close the previously opened "dialog window"
     */
    void closeDialog();
}
