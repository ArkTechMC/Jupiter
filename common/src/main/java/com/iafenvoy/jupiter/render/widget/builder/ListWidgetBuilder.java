package com.iafenvoy.jupiter.render.widget.builder;

import com.iafenvoy.jupiter.config.entry.ListBaseEntry;
import com.iafenvoy.jupiter.render.screen.dialog.ListDialog;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ListWidgetBuilder<T> extends WidgetBuilder<List<T>> {
    protected final ListBaseEntry<T> config;
    @Nullable
    private ButtonWidget button;

    public ListWidgetBuilder(ListBaseEntry<T> config) {
        super(config);
        this.config = config;
    }

    @Override
    public void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        MinecraftClient client = CLIENT.get();
        this.button = ButtonWidget.builder(Text.of(String.valueOf(this.config.getValue())), button -> client.setScreen(new ListDialog<>(client.currentScreen, this.config))).dimensions(x, y, width, height).build();
        appender.accept(this.button);
    }

    @Override
    public void updateCustom(boolean visible, int y) {
        if (this.button == null) return;
        this.button.visible = visible;
        this.button.setY(y);
    }

    @Override
    public void refresh() {
        if (this.button == null) return;
        this.button.setMessage(Text.of(String.valueOf(this.config.getValue())));
    }
}
