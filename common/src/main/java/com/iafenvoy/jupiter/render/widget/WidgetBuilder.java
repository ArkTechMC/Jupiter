package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class WidgetBuilder<T> {
    protected static final Supplier<MinecraftClient> CLIENT = MinecraftClient::getInstance;
    protected final IConfigEntry<T> config;
    private TextWidget textWidget;
    private ButtonWidget resetButton;
    protected boolean canSave = true;

    protected WidgetBuilder(IConfigEntry<T> config) {
        this.config = config;
    }

    public void addDialogElements(Consumer<ClickableWidget> appender, String text, int x, int y, int width, int height) {
        TextRenderer textRenderer = CLIENT.get().textRenderer;
        this.textWidget = new TextWidget(20, y, textRenderer.getWidth(text), height, Text.of(text), textRenderer);
        appender.accept(this.textWidget);
        this.resetButton = ButtonWidget.builder(Text.translatable("malilib.gui.button.remove"), button -> {
            this.config.reset();
            this.refresh();
        }).dimensions(x + width - 50, y, 50, height).build();
        this.refreshResetButton(true);
        appender.accept(this.resetButton);
        this.addCustomElements(appender, x, y, width - 55, height);
    }

    public void addElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        String name = I18n.translate(this.config.getNameKey());
        TextRenderer textRenderer = CLIENT.get().textRenderer;
        this.textWidget = new TextWidget(20, y, textRenderer.getWidth(name), height, Text.of(name), textRenderer);
        appender.accept(this.textWidget);
        this.resetButton = ButtonWidget.builder(Text.translatable("malilib.gui.button.reset"), button -> {
            this.config.reset();
            this.refresh();
        }).dimensions(x + width - 50, y, 50, height).build();
        this.refreshResetButton(false);
        this.config.registerCallback(v -> this.refreshResetButton(false));
        appender.accept(this.resetButton);
        this.addCustomElements(appender, x, y, width - 55, height);
    }

    private void refreshResetButton(boolean dialog) {
        this.setCanReset(dialog || !this.config.getValue().equals(this.config.getDefaultValue()));
    }

    protected void setCanReset(boolean b) {
        this.resetButton.active = b;
    }

    public abstract void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height);

    public void update(boolean visible, int y) {
        if (this.textWidget != null) {
            this.textWidget.visible = visible;
            this.textWidget.setY(y);
        }
        if (this.resetButton != null) {
            this.resetButton.visible = visible;
            this.resetButton.setY(y);
        }
        this.updateCustom(visible, y);
    }

    public abstract void updateCustom(boolean visible, int y);

    public abstract void refresh();

    public boolean canSave() {
        return canSave;
    }
}
