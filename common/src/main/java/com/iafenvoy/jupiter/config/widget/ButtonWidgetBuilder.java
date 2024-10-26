package com.iafenvoy.jupiter.config.widget;

import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

public class ButtonWidgetBuilder<T> implements IWidgetBuilder<T> {
    private final Function<IConfigBase<T>, ButtonWidget.PressAction> action;

    public ButtonWidgetBuilder(Function<IConfigBase<T>, ButtonWidget.PressAction> action) {
        this.action = action;
    }

    @Override
    public ClickableWidget build(int x, int y, int width, int height, IConfigBase<T> parent) {
        return ButtonWidget.builder(Text.translatable(parent.getNameKey()), this.action.apply(parent)).position(x, y).size(width, height).build();
    }
}
