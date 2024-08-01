package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.config.AbstractConfigContainer;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase;
import com.iafenvoy.jupiter.malilib.gui.GuiHorizontalScrollBar;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonGeneric;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractConfigScreen extends GuiConfigsBase {
    protected final AbstractConfigContainer configContainer;
    protected final List<TabButton> tabButtons = new ArrayList<>();
    protected final GuiHorizontalScrollBar scrollBar = new GuiHorizontalScrollBar();
    protected boolean needScrollBar = false;
    private int currentTab = 0;

    public AbstractConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(10, 50, configContainer.getConfigId(), parent, configContainer.getTitleNameKey());
        this.configContainer = configContainer;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        int x = 10, y = 22;
        // tab buttons are set
        this.tabButtons.clear();
        List<AbstractConfigContainer.ConfigCategory> configTabs = this.configContainer.getConfigTabs();
        for (int i = 0; i < configTabs.size(); i++) {
            AbstractConfigContainer.ConfigCategory category = configTabs.get(i);
            TabButton tabButton = this.addButton(new TabButton(category, x, y, -1, 20, StringUtils.translate(category.translateKey())), (buttonBase, listener) -> {
                this.onSettingsChanged();
                // reload the GUI when tab button is clicked
                currentTab = this.configContainer.getConfigTabs().indexOf(((TabButton) buttonBase).category);
                buttonBase.setEnabled(false);
                this.reCreateListWidget();
                //noinspection ConstantConditions
                this.getListWidget().resetScrollbarPosition();
                this.initGui();
            });
            tabButton.setEnabled(i != currentTab);
            this.tabButtons.add(tabButton);
            x += tabButton.getWidth() + 2;
        }
        x += 10;
        this.needScrollBar = x > this.width - 20;
        this.scrollBar.setMaxValue(Math.max(0, x - this.width));
    }

    protected void updateTabPos() {
        for (TabButton button : this.tabButtons)
            button.updatePos(this.scrollBar.getValue());
    }

    @Override
    public void tick() {
        super.tick();
        this.updateTabPos();
    }

    @Override
    public Identifier getConfigId() {
        return this.configContainer.getConfigId();
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
    public boolean onMouseScrolled(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
        if (super.onMouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) return true;
        if (mouseX >= 10 && mouseX <= this.width - 20 && mouseY >= 22 && mouseY <= 42) {
            this.scrollBar.setValue(this.scrollBar.getValue() + (horizontalAmount > 0 ? -20 : 20));
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        this.closeGui(true);
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(this.configContainer.getConfigTabs().get(currentTab).getConfigs());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        super.render(drawContext, mouseX, mouseY, partialTicks);
        String currentText = this.getCurrentEditText();
        int textWidth = this.textRenderer.getWidth(currentText);
        drawContext.drawTextWithShadow(this.textRenderer, currentText, this.width - textWidth - 10, 10, -1);
        if (this.needScrollBar)
            this.scrollBar.render(mouseX, mouseY, partialTicks, 10, 43, this.width - 20, 8, this.width + this.scrollBar.getMaxValue());
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.scrollBar.wasMouseOver()) {
            this.scrollBar.setIsDragging(true);
            return true;
        }
        return super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            this.scrollBar.setIsDragging(false);
        return super.onMouseReleased(mouseX, mouseY, mouseButton);
    }

    protected abstract String getCurrentEditText();

    public static class TabButton extends ButtonGeneric {
        private final AbstractConfigContainer.ConfigCategory category;
        private final int baseX;

        public TabButton(AbstractConfigContainer.ConfigCategory category, int baseX, int y, int width, int height, String text, String... hoverStrings) {
            super(baseX, y, width, height, text, hoverStrings);
            this.category = category;
            this.baseX = baseX;
        }

        public void updatePos(int offsetX) {
            this.x = this.baseX - offsetX;
        }

        @Override
        public boolean onMouseScrolledImpl(int mouseX, int mouseY, double horizontalAmount, double verticalAmount) {
            return false;
        }
    }
}
