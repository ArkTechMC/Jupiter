package com.iafenvoy.jupiter.render.screen.dialog;

import com.iafenvoy.jupiter.config.entry.ListBaseEntry;
import com.iafenvoy.jupiter.render.screen.WidgetBuilderManager;
import com.iafenvoy.jupiter.render.screen.scrollbar.VerticalScrollBar;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ListDialog<T> extends Dialog<List<T>> {
    protected final ListBaseEntry<T> entry;
    protected final VerticalScrollBar itemScrollBar = new VerticalScrollBar();
    protected final List<WidgetBuilder<T>> widgets = new ArrayList<>();
    private int configPerPage;

    public ListDialog(Screen parent, ListBaseEntry<T> entry) {
        super(parent, entry);
        this.entry = entry;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(Text.of("<"), button -> this.close()).dimensions(10, 5, 20, 15).build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("+"), button -> {
            this.entry.getValue().add(this.entry.newValue());
            this.clearAndInit();
        }).dimensions(this.width - 60, 5, 20, 20).build());
        this.calculateMaxItems();
        this.widgets.clear();
        List<T> values = this.entry.getValue();
        for (int i = 0; i < values.size(); i++) {
            WidgetBuilder<T> widget = WidgetBuilderManager.get(this.entry.newSingleInstance(values.get(i), i, this::clearAndInit));
            this.widgets.add(widget);
            widget.addDialogElements(this::addDrawableChild, i + ":", 40, 0, Math.max(10, this.width - 70), ITEM_HEIGHT);
        }
        this.updateItemPos();
    }

    public void calculateMaxItems() {
        this.configPerPage = Math.max(0, (this.height - 25) / (ITEM_HEIGHT + ITEM_SEP));
        this.itemScrollBar.setMaxValue(Math.max(0, this.entry.getValue().size() - this.configPerPage));
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.calculateMaxItems();
        this.updateItemPos();
    }

    public void updateItemPos() {
        int top = this.itemScrollBar.getValue();
        List<T> entries = this.entry.getValue();
        for (int i = 0; i < top && i < entries.size(); i++)
            this.widgets.get(i).update(false, 0);
        for (int i = top; i < top + this.configPerPage && i < entries.size(); i++)
            this.widgets.get(i).update(true, 25 + ITEM_SEP + (i - top) * (ITEM_HEIGHT + ITEM_SEP));
        for (int i = top + this.configPerPage; i < entries.size(); i++)
            this.widgets.get(i).update(false, 0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) return true;
        this.itemScrollBar.setValue(this.itemScrollBar.getValue() + (amount > 0 ? -1 : 1) * ITEM_PER_SCROLL);
        this.updateItemPos();
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, partialTicks);
        this.itemScrollBar.render(mouseX, mouseY, partialTicks, this.width - 18, 25, 8, this.height - 50, (this.configPerPage + this.itemScrollBar.getMaxValue()) * (ITEM_HEIGHT + ITEM_SEP));
        if (this.itemScrollBar.isDragging()) this.updateItemPos();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && this.itemScrollBar.wasMouseOver()) {
            this.itemScrollBar.setIsDragging(true);
            this.updateItemPos();
            return true;
        }
        boolean b = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!b) this.setFocused(null);
        return b;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0) this.itemScrollBar.setIsDragging(false);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
