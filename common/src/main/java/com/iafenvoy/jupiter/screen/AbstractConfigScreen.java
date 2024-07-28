package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.config.AbstractConfigContainer;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonGeneric;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class AbstractConfigScreen extends GuiConfigsBase {
    private static int currentTab = 0;
    protected final AbstractConfigContainer configContainer;

    public AbstractConfigScreen(Screen parent, AbstractConfigContainer configContainer) {
        super(10, 50, configContainer.getModId(), parent, configContainer.getTitleNameKey());
        this.configContainer = configContainer;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        int x = 10, y = 26;
        // tab buttons are set
        List<AbstractConfigContainer.ConfigCategory> configTabs = this.configContainer.getConfigTabs();
        for (int i = 0; i < configTabs.size(); i++) {
            AbstractConfigContainer.ConfigCategory category = configTabs.get(i);
            TabButton tabButton = new TabButton(category, x, y, -1, 20, StringUtils.translate(category.translateKey()));
            tabButton.setEnabled(i != currentTab);
            this.addButton(tabButton, (buttonBase, listener) -> {
                this.onSettingsChanged();
                // reload the GUI when tab button is clicked
                currentTab = this.configContainer.getConfigTabs().indexOf(((TabButton) buttonBase).category);
                buttonBase.setEnabled(false);
                this.reCreateListWidget();
                //noinspection ConstantConditions
                this.getListWidget().resetScrollbarPosition();
                this.initGui();
            });
            x += tabButton.getWidth() + 2;
        }
    }

    @Override
    public String getModId() {
        return this.configContainer.getModId();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        this.closeGui(true);
    }

    @Override
    public boolean shouldPause() {
        return true;
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
    }

    protected abstract String getCurrentEditText();

    public static class TabButton extends ButtonGeneric {
        private final AbstractConfigContainer.ConfigCategory category;

        public TabButton(AbstractConfigContainer.ConfigCategory category, int x, int y, int width, int height, String text, String... hoverStrings) {
            super(x, y, width, height, text, hoverStrings);
            this.category = category;
        }
    }
}
