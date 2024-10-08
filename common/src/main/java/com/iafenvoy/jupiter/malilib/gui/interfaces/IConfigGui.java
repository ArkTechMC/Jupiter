package com.iafenvoy.jupiter.malilib.gui.interfaces;

import com.iafenvoy.jupiter.malilib.config.gui.ButtonPressDirtyListenerSimple;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IConfigGui {
    /**
     * Returns the Mod ID of the mod to which the configs on this GUI belong to
     *
     */
    Identifier getConfigId();

    /**
     * When called, the implementer should clear all the stored config options
     * and any associated change listeners etc.
     */
    void clearOptions();

    /**
     * Returns a list of all currently visible/available/selected/whatever config options
     * that the widget list can use.
     *
     */
    List<GuiConfigsBase.ConfigOptionWrapper> getConfigs();

    /**
     * Returns a simple dirty listener for button presses. The configs will be interpreted
     * as dirty, if the listener saw even one button press.
     *
     */
    ButtonPressDirtyListenerSimple getButtonPressListener();

    /**
     * Get the "dialog window" handler for this GUI, if any.
     *
     */
    @Nullable
    default IDialogHandler getDialogHandler() {
        return null;
    }

    /**
     * Returns an info provider to get customized hover tooltips for the configs
     *
     */
    @Nullable
    IConfigInfoProvider getHoverInfoProvider();
}
