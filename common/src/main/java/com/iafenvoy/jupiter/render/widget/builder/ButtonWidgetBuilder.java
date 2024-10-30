package com.iafenvoy.jupiter.render.widget.builder;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ButtonWidgetBuilder<T> extends WidgetBuilder<T> {
    private final ButtonWidget.PressAction action;
    private final Supplier<Text> nameSupplier;
    @Nullable
    private ButtonWidget button;

    public ButtonWidgetBuilder(IConfigEntry<T> config, ButtonWidget.PressAction action, Supplier<Text> nameSupplier) {
        super(config);
        this.action = button -> {
            action.onPress(button);
            this.refresh();
        };
        this.nameSupplier = nameSupplier;
    }

    @Override
    public void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        this.button = ButtonWidget.builder(this.nameSupplier.get(), this.action).dimensions(x, y, width, height).build();
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
        this.button.setMessage(this.nameSupplier.get());
    }
}
