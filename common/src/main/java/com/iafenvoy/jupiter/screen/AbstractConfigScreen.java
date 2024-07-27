package com.iafenvoy.jupiter.screen;

import com.iafenvoy.jupiter.api.ConfigData;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class AbstractConfigScreen extends GuiConfigsBase {
    private static int currentTab = 0;
    private final ConfigData configData;

    public AbstractConfigScreen(Screen parent, ConfigData configData) {
        super(10, 50, configData.getModId(), parent, configData.getTitleNameKey());
        this.configData = configData;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        int x = 10, y = 26;
        // tab buttons are set
        List<ConfigData.ConfigCategory> configTabs = this.configData.getConfigTabs();
        for (int i = 0; i < configTabs.size(); i++) {
            ConfigData.ConfigCategory category = configTabs.get(i);
            TabButton tabButton = new TabButton(category, x, y, -1, 20, StringUtils.translate(category.translateKey()));
            tabButton.setEnabled(i != currentTab);
            this.addButton(tabButton, (buttonBase, listener) -> {
                this.onSettingsChanged();
                // reload the GUI when tab button is clicked
                currentTab = this.configData.getConfigTabs().indexOf(((TabButton) buttonBase).category);
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
        return this.configData.getModId();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) this.close();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        this.closeGui(true);
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(this.configData.getConfigTabs().get(currentTab).getConfigs());
    }

    public static class TabButton extends ButtonGeneric {
        private final ConfigData.ConfigCategory category;

        public TabButton(ConfigData.ConfigCategory category, int x, int y, int width, int height, String text, String... hoverStrings) {
            super(x, y, width, height, text, hoverStrings);
            this.category = category;
        }
    }
}
