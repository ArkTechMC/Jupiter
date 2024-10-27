package com.iafenvoy.jupiter.client.widget;

import com.iafenvoy.jupiter.interfaces.IConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TextWidgetBuilder<T> implements IWidgetBuilder<T> {
    @Override
    public @Nullable ClickableWidget build(int x, int y, int width, int height, IConfigBase<T> parent) {
        return new TextFieldWidget(CLIENT.get().textRenderer, x, y, width, height, Text.of(parent.getValue().toString()));
    }
}
