package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.config.ConfigGroup;
import com.iafenvoy.jupiter.config.container.AbstractConfigContainer;
import com.iafenvoy.jupiter.config.container.FakeConfigContainer;
import com.iafenvoy.jupiter.interfaces.IConfig;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.screen.scrollbar.HorizontalScrollBar;
import com.iafenvoy.jupiter.render.screen.scrollbar.VerticalScrollBar;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class ConfigScreen extends Screen implements IConfig {
    private static final int ITEM_PER_SCROLL = 2;
    private static final int ITEM_HEIGHT = 20;
    private static final int ITEM_SEP = 5;
    private final Screen parent;
    protected final AbstractConfigContainer configContainer;
    protected final List<TabButton> groupButtons = new ArrayList<>();
    protected final List<WidgetBuilder<?, ?>> configWidgets = new ArrayList<>();
    protected final HorizontalScrollBar groupScrollBar = new HorizontalScrollBar();
    protected final VerticalScrollBar itemScrollBar = new VerticalScrollBar();
    protected boolean needScrollBar = false;
    private int currentTab = 0;
    private ConfigGroup currentGroup;
    private int configPerPage;
    private int textMaxLength;

    public ConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(Text.translatable(configContainer.getTitleNameKey()));
        this.parent = parent;
        this.configContainer = configContainer;
        this.currentGroup = configContainer.getConfigTabs().get(0);
    }

    @Override
    protected void init() {
        super.init();
        int x = 10, y = 22;
        this.groupButtons.clear();
        List<ConfigGroup> configTabs = this.configContainer.getConfigTabs();
        for (int i = 0; i < configTabs.size(); i++) {
            ConfigGroup category = configTabs.get(i);
            TabButton tabButton = this.addDrawableChild(new TabButton(category, x, y, this.textRenderer.getWidth(I18n.translate(category.getTranslateKey())), 20, button -> {
                currentTab = this.configContainer.getConfigTabs().indexOf(button.group);
                this.currentGroup = button.group;
                this.clearAndInit();
            }));
            tabButton.active = i != currentTab;
            this.groupButtons.add(tabButton);
            x += tabButton.getWidth() + 2;
        }
        x += 10;
        this.needScrollBar = x > this.width - 20;
        this.groupScrollBar.setMaxValue(Math.max(0, x - this.width));
        this.calculateMaxItems();
        this.configWidgets.clear();
        this.configWidgets.addAll(this.currentGroup.getConfigs().stream().map(WidgetBuilderManager::get).toList());
        this.textMaxLength = this.currentGroup.getConfigs().stream().map(IConfigEntry::getNameKey).map(I18n::translate).map(t -> this.textRenderer.getWidth(t)).max(Comparator.naturalOrder()).orElse(0) + 10;
        this.updateItemPos();
    }

    protected void updateTabPos() {
        for (TabButton button : this.groupButtons)
            button.updatePos(this.groupScrollBar.getValue());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.calculateMaxItems();
        this.updateItemPos();
    }

    public void calculateMaxItems() {
        int height = this.height - 45;
        this.configPerPage = Math.max(0, height / (ITEM_HEIGHT + ITEM_SEP));
        this.itemScrollBar.setMaxValue(this.configPerPage);
    }

    public void updateItemPos() {
        int top = this.itemScrollBar.getValue();
        List<IConfigEntry<?>> entries = this.currentGroup.getConfigs();
        for (int i = 0; i < top && i < entries.size(); i++)
            this.configWidgets.get(i).update(false, this.textMaxLength, 0, 10);
        for (int i = top; i < top + this.configPerPage && i < entries.size(); i++)
            this.configWidgets.get(i).update(true, this.textMaxLength, 45 + ITEM_SEP + (i - top) * (ITEM_HEIGHT + ITEM_SEP), Math.max(10, this.width - this.textMaxLength - 30));
    }

    @Override
    public void tick() {
        super.tick();
//        this.updateTabPos();
    }

    @Override
    public Identifier getConfigId() {
        return this.configContainer.getConfigId();
    }

    @Override
    public String getTitleNameKey() {
        return this.configContainer.getTitleNameKey();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) return true;
        if (mouseX >= 10 && mouseX <= this.width - 20 && mouseY >= 22 && mouseY <= 42) {
            this.groupScrollBar.setValue(this.groupScrollBar.getValue() + (amount > 0 ? -20 : 20));
            this.updateTabPos();
            return true;
        } else if (mouseY > 42) {
            this.itemScrollBar.setValue(this.itemScrollBar.getValue() + (amount > 0 ? -1 : 1) * ITEM_PER_SCROLL);
            this.updateItemPos();
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        if (this.configContainer instanceof FakeConfigContainer)
            this.configContainer.onConfigsChanged();
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, partialTicks);
        context.drawText(this.textRenderer, this.title, 20, 10, -1, true);
        String currentText = this.getCurrentEditText();
        int textWidth = this.textRenderer.getWidth(currentText);
        context.drawTextWithShadow(this.textRenderer, currentText, this.width - textWidth - 10, 10, -1);
        if (this.needScrollBar)
            this.groupScrollBar.render(mouseX, mouseY, partialTicks, 10, 43, this.width - 20, 8, this.width + this.groupScrollBar.getMaxValue());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && this.groupScrollBar.wasMouseOver()) {
            this.groupScrollBar.setIsDragging(true);
            this.updateTabPos();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0)
            this.groupScrollBar.setIsDragging(false);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    protected abstract String getCurrentEditText();

    public static class TabButton extends ButtonWidget {
        private final ConfigGroup group;
        private final int baseX;

        public TabButton(ConfigGroup group, int baseX, int y, int width, int height, Consumer<TabButton> listener) {
            super(baseX, y, width, height, Text.translatable(group.getTranslateKey()), button -> listener.accept((TabButton) button), DEFAULT_NARRATION_SUPPLIER);
            this.group = group;
            this.baseX = baseX;
        }

        public void updatePos(int offsetX) {
            this.setX(this.baseX - offsetX);
        }
    }
}