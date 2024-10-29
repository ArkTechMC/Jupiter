package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.ITextFieldConfig;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TextFieldWidgetBuilder<T> extends WidgetBuilder<T, TextWidget> {
    private final ITextFieldConfig textFieldConfig;
    @Nullable
    private TextWidget widget;

    public TextFieldWidgetBuilder(IConfigEntry<T> config) {
        super(config);
        if (config instanceof ITextFieldConfig t) this.textFieldConfig = t;
        else throw new IllegalArgumentException("TextFieldWidgetBuilder only accept ITextFieldConfig");
    }

    @Override
    public void addElements(Function<TextWidget, TextWidget> appender, int x, int y, int width, int height) {
        this.widget = appender.apply(new TextWidget(x, y, width, height, Text.of(this.textFieldConfig.valueAsString()), CLIENT.get().textRenderer));
    }

    @Override
    public void update(boolean visible, int x, int y, int width) {
        if (this.widget == null) return;
        this.widget.visible = visible;
        this.widget.setPosition(x, y);
        this.widget.setWidth(width);
    }
}
