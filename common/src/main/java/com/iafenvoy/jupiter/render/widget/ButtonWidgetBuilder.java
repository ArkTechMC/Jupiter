package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ButtonWidgetBuilder<T> implements IWidgetBuilder<T> {
    private final Function<IConfigBase<T>, ButtonWidget.PressAction> action;

    public ButtonWidgetBuilder(Function<IConfigBase<T>, ButtonWidget.PressAction> action) {
        this.action = action;
    }

    @Override
    public @Nullable ClickableWidget build(int x, int y, int width, int height, IConfigBase<T> parent) {
        return ButtonWidget.builder(Text.translatable(parent.getNameKey()), this.action.apply(parent)).position(x, y).size(width, height).build();
    }
}
