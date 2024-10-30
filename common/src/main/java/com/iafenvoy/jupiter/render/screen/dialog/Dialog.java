package com.iafenvoy.jupiter.render.screen.dialog;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.screen.IJupiterScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class Dialog<T> extends Screen implements IJupiterScreen {
    protected final IConfigEntry<T> entry;
    private final Screen parent;

    protected Dialog(Screen parent, IConfigEntry<T> entry) {
        super(Text.translatable(entry.getNameKey()));
        this.parent = parent;
        this.entry = entry;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        context.drawText(this.textRenderer, this.title, 35, 10, -1, true);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
