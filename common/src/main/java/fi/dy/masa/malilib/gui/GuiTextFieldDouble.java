package fi.dy.masa.malilib.gui;

import net.minecraft.client.font.TextRenderer;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class GuiTextFieldDouble extends GuiTextFieldGeneric {
    private static final Pattern PATTER_NUMBER = Pattern.compile("^-?([0-9]+(\\.[0-9]*)?)?");

    public GuiTextFieldDouble(int x, int y, int width, int height, TextRenderer fontRenderer) {
        super(x, y, width, height, fontRenderer);

        this.setTextPredicate(new Predicate<String>() {
            @Override
            public boolean test(String input) {
                return input.length() <= 0 || PATTER_NUMBER.matcher(input).matches();
            }
        });
    }
}
