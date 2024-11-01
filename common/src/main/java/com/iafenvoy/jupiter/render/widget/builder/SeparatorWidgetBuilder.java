package com.iafenvoy.jupiter.render.widget.builder;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import com.mojang.datafixers.util.Unit;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class SeparatorWidgetBuilder extends WidgetBuilder<Unit> {
    public SeparatorWidgetBuilder(IConfigEntry<Unit> config) {
        super(config);
    }

    @Override
    public void addElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        width = width + x - 20;
        TextRenderer textRenderer = CLIENT.get().textRenderer;
        int w = textRenderer.getWidth("-"), k = 0;
        while ((k + 1) * w <= width) k++;
        String name = "-".repeat(k);
        this.textWidget = new TextWidget(20, y, textRenderer.getWidth(name), height, Text.of(name), textRenderer);
        appender.accept(this.textWidget);
    }

    @Override
    public void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        //No Need
    }

    @Override
    public void updateCustom(boolean visible, int y) {
        //No Need
    }

    @Override
    public void refresh() {
        //No Need
    }
}
