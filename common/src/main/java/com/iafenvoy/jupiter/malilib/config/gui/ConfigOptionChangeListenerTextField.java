package com.iafenvoy.jupiter.malilib.config.gui;

import com.iafenvoy.jupiter.malilib.config.IStringRepresentable;
import com.iafenvoy.jupiter.malilib.gui.GuiTextFieldGeneric;
import com.iafenvoy.jupiter.malilib.gui.interfaces.ITextFieldListener;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonBase;

public class ConfigOptionChangeListenerTextField implements ITextFieldListener<GuiTextFieldGeneric> {
    protected final IStringRepresentable config;
    protected final GuiTextFieldGeneric textField;
    protected final ButtonBase buttonReset;

    public ConfigOptionChangeListenerTextField(IStringRepresentable config, GuiTextFieldGeneric textField, ButtonBase buttonReset) {
        this.config = config;
        this.textField = textField;
        this.buttonReset = buttonReset;
    }

    @Override
    public boolean onTextChange(GuiTextFieldGeneric textField) {
        this.buttonReset.setEnabled(this.config.isModified(this.textField.getText()));
        return false;
    }
}
