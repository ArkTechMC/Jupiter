package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ButtonWidgetBuilder<T> extends WidgetBuilder<T, ButtonWidget> {
    private final ButtonWidget.PressAction action;
    @Nullable
    private ButtonWidget button;

    public ButtonWidgetBuilder(IConfigEntry<T> config, ButtonWidget.PressAction action) {
        super(config);
        this.action = action;
    }

    @Override
    public void addElements(Function<ButtonWidget, ButtonWidget> appender, int x, int y, int width, int height) {
        this.button = appender.apply(ButtonWidget.builder(Text.translatable(this.config.getNameKey()), this.action).dimensions(x, y, width, height).build());
    }

    @Override
    public void update(boolean visible, int x, int y, int width) {
        if (this.button == null) return;
        this.button.visible = visible;
        this.button.setPosition(x, y);
        this.button.setWidth(width);
    }
}
