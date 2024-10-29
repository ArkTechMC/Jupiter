package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class WidgetBuilder<T, E extends ClickableWidget> {
    protected static final Supplier<MinecraftClient> CLIENT = MinecraftClient::getInstance;
    protected final IConfigEntry<T> config;
    protected final List<ValueChangeCallback<T, E>> callbacks = new ArrayList<>();
    private TextWidget textWidget;
    private ButtonWidget resetButton;
    protected boolean canSave = true;

    protected WidgetBuilder(IConfigEntry<T> config) {
        this.config = config;
    }

    public WidgetBuilder<T, E> registerCallback(ValueChangeCallback<T, E> callback) {
        this.callbacks.add(callback);
        return this;
    }

    public void invokeCallback(E widget) {
        this.callbacks.forEach(x -> x.onValueChange(this.config, widget));
    }

    public void addElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        String name = I18n.translate(this.config.getNameKey());
        TextRenderer textRenderer = CLIENT.get().textRenderer;
        this.textWidget = new TextWidget(20, y, textRenderer.getWidth(name), height, Text.of(name), textRenderer);
        appender.accept(this.textWidget);
        this.resetButton = ButtonWidget.builder(Text.translatable("malilib.gui.button.reset"), button -> {
            this.config.reset();
            this.refresh();
        }).dimensions(x + width - 30, y, 30, height).build();
        this.refreshResetButton();
        this.config.registerCallback(v -> this.refreshResetButton());
        appender.accept(this.resetButton);
        this.addCustomElements(appender, x, y, width - 35, height);
    }

    private void refreshResetButton() {
        this.setCanReset(this.config.getValue() != this.config.getDefaultValue());
    }

    protected void setCanReset(boolean b) {
        this.resetButton.active = b;
    }

    public abstract void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height);

    public void update(boolean visible, int y) {
        this.textWidget.visible = visible;
        this.textWidget.setY(y);
        this.resetButton.visible = visible;
        this.resetButton.setY(y);
        this.updateCustom(visible, y);
    }

    public abstract void updateCustom(boolean visible, int y);

    public abstract void refresh();

    @FunctionalInterface
    public interface ValueChangeCallback<T, E extends Drawable & Widget & Selectable> {
        void onValueChange(IConfigEntry<T> config, E widget);
    }

    public boolean canSave() {
        return canSave;
    }
}
