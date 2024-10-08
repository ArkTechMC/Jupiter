package com.iafenvoy.jupiter.malilib.gui;

import net.minecraft.client.font.TextRenderer;

import java.util.regex.Pattern;

public class GuiTextFieldDouble extends GuiTextFieldGeneric {
    private static final Pattern PATTER_NUMBER = Pattern.compile("^-?([0-9]+(\\.[0-9]*)?)?");

    public GuiTextFieldDouble(int x, int y, int width, int height, TextRenderer fontRenderer) {
        super(x, y, width, height, fontRenderer);

        this.setTextPredicate(input -> input.isEmpty() || PATTER_NUMBER.matcher(input).matches());
    }
}
