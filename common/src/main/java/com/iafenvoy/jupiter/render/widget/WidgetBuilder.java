package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class WidgetBuilder<T, E extends Drawable & Widget & Selectable> {
    protected static final Supplier<MinecraftClient> CLIENT = MinecraftClient::getInstance;
    protected final IConfigEntry<T> config;
    protected final List<ValueChangeCallback<T, E>> callbacks = new ArrayList<>();

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

    public abstract void addElements(Function<E, E> appender, int x, int y, int width, int height);

    public abstract void update(boolean visible, int x, int y, int width);

    @FunctionalInterface
    public interface ValueChangeCallback<T, E extends Drawable & Widget & Selectable> {
        void onValueChange(IConfigEntry<T> config, E widget);
    }
}
