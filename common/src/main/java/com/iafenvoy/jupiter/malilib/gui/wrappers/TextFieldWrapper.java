package com.iafenvoy.jupiter.malilib.gui.wrappers;

import com.iafenvoy.jupiter.malilib.gui.GuiTextFieldGeneric;
import com.iafenvoy.jupiter.malilib.gui.interfaces.ITextFieldListener;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public record TextFieldWrapper<T extends GuiTextFieldGeneric>(T textField, ITextFieldListener<T> listener) {

    public boolean isFocused() {
        return this.textField.isFocused();
    }

    public void setFocused(boolean isFocused) {
        this.textField.setFocused(isFocused);
    }

    public void onGuiClosed() {
        if (this.listener != null) {
            this.listener.onGuiClosed(this.textField);
        }
    }

    public void draw(int mouseX, int mouseY, DrawContext drawContext) {
        this.textField.render(drawContext, mouseX, mouseY, 0f);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.textField.mouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }

        if (!this.textField.isMouseOver(mouseX, mouseY)) {
            this.textField.setFocused(false);
        }

        return false;
    }

    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        String textPre = this.textField.getText();

        if (this.textField.isFocused() && this.textField.keyPressed(keyCode, scanCode, modifiers)) {
            if (this.listener != null &&
                    (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_TAB ||
                            !this.textField.getText().equals(textPre))) {
                this.listener.onTextChange(this.textField);
            }

            return true;
        }

        return false;
    }

    public boolean onCharTyped(char charIn, int modifiers) {
        String textPre = this.textField.getText();

        if (this.textField.isFocused() && this.textField.charTyped(charIn, modifiers)) {
            if (this.listener != null && !this.textField.getText().equals(textPre)) {
                this.listener.onTextChange(this.textField);
            }

            return true;
        }

        return false;
    }
}
