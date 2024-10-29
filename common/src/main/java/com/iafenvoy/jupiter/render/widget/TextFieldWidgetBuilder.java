package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.ITextFieldConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TextFieldWidgetBuilder<T> extends WidgetBuilder<T, TextWidget> {
    private final ITextFieldConfig textFieldConfig;
    @Nullable
    private TextFieldWithErrorWidget widget;

    public TextFieldWidgetBuilder(IConfigEntry<T> config) {
        super(config);
        if (config instanceof ITextFieldConfig t) this.textFieldConfig = t;
        else throw new IllegalArgumentException("TextFieldWidgetBuilder only accept ITextFieldConfig");
    }

    @Override
    public void addCustomElements(Consumer<ClickableWidget> appender, int x, int y, int width, int height) {
        this.widget = new TextFieldWithErrorWidget(CLIENT.get().textRenderer, x, y, width, height);
        this.widget.setText(this.textFieldConfig.valueAsString());
        this.widget.setChangedListener(s -> {
            try {
                this.textFieldConfig.setValueFromString(s);
                this.canSave = true;
                this.widget.setHasError(false);
            } catch (Exception ignored) {
                this.canSave = false;
                this.widget.setHasError(true);
                this.setCanReset(true);
            }
        });
        appender.accept(this.widget);
    }

    @Override
    public void updateCustom(boolean visible, int y) {
        if (this.widget == null) return;
        this.widget.visible = visible;
        this.widget.setY(y);
    }

    @Override
    public void refresh() {
        if (this.widget == null) return;
        this.widget.setText(this.textFieldConfig.valueAsString());
    }

    private static class TextFieldWithErrorWidget extends TextFieldWidget {
        private boolean hasError = false;

        public TextFieldWithErrorWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
            super(textRenderer, x, y, width, height, Text.empty());
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.hasError) {
                this.setUneditableColor(0xFFFF0000);
                this.setEditable(false);
            }
            super.renderButton(context, mouseX, mouseY, delta);
            this.setEditable(true);
        }

        public void setHasError(boolean hasError) {
            this.hasError = hasError;
        }
    }
}
