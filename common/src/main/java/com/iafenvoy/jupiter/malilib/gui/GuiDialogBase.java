package com.iafenvoy.jupiter.malilib.gui;

import net.minecraft.client.MinecraftClient;

public class GuiDialogBase extends GuiBase {
    protected int dialogWidth;
    protected int dialogHeight;
    protected int dialogLeft;
    protected int dialogTop;

    public void setWidthAndHeight(int width, int height) {
        this.dialogWidth = width;
        this.dialogHeight = height;
    }

    public void setPosition(int left, int top) {
        this.dialogLeft = left;
        this.dialogTop = top;
    }

    public void centerOnScreen() {
        int left;
        int top;

        if (this.getParent() != null) {
            left = this.getParent().width / 2 - this.dialogWidth / 2;
            top = this.getParent().height / 2 - this.dialogHeight / 2;
        } else {
            left = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - this.dialogWidth / 2;
            top = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - this.dialogHeight / 2;
        }

        this.setPosition(left, top);
    }
}
