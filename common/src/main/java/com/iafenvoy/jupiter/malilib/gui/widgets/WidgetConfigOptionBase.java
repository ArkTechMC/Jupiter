package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.gui.GuiTextFieldGeneric;
import com.iafenvoy.jupiter.malilib.gui.wrappers.TextFieldWrapper;
import com.iafenvoy.jupiter.malilib.util.KeyCodes;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.iafenvoy.jupiter.malilib.config.IConfigResettable;
import com.iafenvoy.jupiter.malilib.config.gui.ConfigOptionChangeListenerTextField;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Nullable;

public abstract class WidgetConfigOptionBase<TYPE> extends WidgetListEntryBase<TYPE> {
    protected final WidgetListConfigOptionsBase<?, ?> parent;
    @Nullable
    protected TextFieldWrapper<? extends GuiTextFieldGeneric> textField = null;
    @Nullable
    protected String initialStringValue;
    protected int maxTextfieldTextLength = 65535;
    /**
     * The last applied value for any textfield-based configs.
     * Button based (boolean, option-list) values get applied immediately upon clicking the button.
     */
    protected String lastAppliedValue;

    public WidgetConfigOptionBase(int x, int y, int width, int height,
                                  WidgetListConfigOptionsBase<?, ?> parent, TYPE entry, int listIndex) {
        super(x, y, width, height, entry, listIndex);

        this.parent = parent;
    }

    public abstract boolean wasConfigModified();

    public boolean hasPendingModifications() {
        if (this.textField != null) {
            return !this.textField.getTextField().getText().equals(this.lastAppliedValue);
        }

        return false;
    }

    public abstract void applyNewValueToConfig();

    protected GuiTextFieldGeneric createTextField(int x, int y, int width, int height) {
        return new GuiTextFieldGeneric(x + 2, y, width, height, this.textRenderer);
    }

    protected void addTextField(GuiTextFieldGeneric field, ConfigOptionChangeListenerTextField listener) {
        TextFieldWrapper<? extends GuiTextFieldGeneric> wrapper = new TextFieldWrapper<>(field, listener);
        this.textField = wrapper;
        this.parent.addTextField(wrapper);
    }

    protected ButtonGeneric createResetButton(int x, int y, IConfigResettable config) {
        String labelReset = StringUtils.translate("malilib.gui.button.reset.caps");
        ButtonGeneric resetButton = new ButtonGeneric(x, y, -1, 20, labelReset);
        resetButton.setEnabled(config.isModified());

        return resetButton;
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }

        boolean ret = false;

        if (this.textField != null) {
            ret |= this.textField.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (!this.subWidgets.isEmpty()) {
            for (WidgetBase widget : this.subWidgets) {
                ret |= widget.isMouseOver(mouseX, mouseY) && widget.onMouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        if (this.textField != null && this.textField.isFocused()) {
            if (keyCode == KeyCodes.KEY_ENTER) {
                this.applyNewValueToConfig();
                return true;
            } else {
                return this.textField.onKeyTyped(keyCode, scanCode, modifiers);
            }
        }

        return false;
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        if (this.textField != null && this.textField.onCharTyped(charIn, modifiers)) {
            return true;
        }

        return super.onCharTypedImpl(charIn, modifiers);
    }

    @Override
    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    protected void drawTextFields(int mouseX, int mouseY, DrawContext drawContext) {
        if (this.textField != null) {
            this.textField.getTextField().render(drawContext, mouseX, mouseY, 0f);
        }
    }
}
