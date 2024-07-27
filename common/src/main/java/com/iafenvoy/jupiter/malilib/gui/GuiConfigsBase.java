package com.iafenvoy.jupiter.malilib.gui;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.malilib.util.GuiUtils;
import com.iafenvoy.jupiter.malilib.util.KeyCodes;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.gui.ButtonPressDirtyListenerSimple;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase.ConfigOptionWrapper;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigGui;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigInfoProvider;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IDialogHandler;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetConfigOption;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetListConfigOptions;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GuiConfigsBase extends GuiListBase<ConfigOptionWrapper, WidgetConfigOption, WidgetListConfigOptions> implements IConfigGui {
    protected final List<Runnable> hotkeyChangeListeners = new ArrayList<>();
    protected final ButtonPressDirtyListenerSimple dirtyListener = new ButtonPressDirtyListenerSimple();
    protected final String modId;
    protected int configWidth = 204;
    @Nullable
    protected IConfigInfoProvider hoverInfoProvider;
    @Nullable
    protected IDialogHandler dialogHandler;

    public GuiConfigsBase(int listX, int listY, String modId, @Nullable Screen parent, String titleKey, Object... args) {
        super(listX, listY);

        this.modId = modId;
        this.title = StringUtils.translate(titleKey, args);
    }

    @Override
    protected int getBrowserWidth() {
        return this.width - 20;
    }

    @Override
    protected int getBrowserHeight() {
        return this.height - 80;
    }

    protected boolean useKeybindSearch() {
        return false;
    }

    protected int getConfigWidth() {
        return this.configWidth;
    }

    public GuiConfigsBase setConfigWidth(int configWidth) {
        this.configWidth = configWidth;
        return this;
    }

    public GuiConfigsBase setHoverInfoProvider(IConfigInfoProvider provider) {
        this.hoverInfoProvider = provider;
        return this;
    }

    @Override
    protected WidgetListConfigOptions createListWidget(int listX, int listY) {
        return new WidgetListConfigOptions(listX, listY,
                this.getBrowserWidth(), this.getBrowserHeight(), this.getConfigWidth(), 0.f, this.useKeybindSearch(), this);
    }

    @Override
    public void removed() {
        if (this.getListWidget().wereConfigsModified()) {
            this.getListWidget().applyPendingModifications();
            this.onSettingsChanged();
            this.getListWidget().clearConfigsModifiedFlag();
        }
    }

    protected void onSettingsChanged() {
        ConfigManager.getInstance().onConfigsChanged(this.modId);
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (this.getListWidget().onKeyTyped(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (keyCode == KeyCodes.KEY_ESCAPE && this.getParent() != GuiUtils.getCurrentScreen()) {
            this.closeGui(true);
            return true;
        }

        return false;
    }

    @Override
    public boolean onCharTyped(char charIn, int modifiers) {
        if (this.getListWidget().onCharTyped(charIn, modifiers)) {
            return true;
        }

        return super.onCharTyped(charIn, modifiers);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClicked(mouseX, mouseY, mouseButton)) {
            return true;
        }
        return false;
    }

    protected void updateKeybindButtons() {
        for (Runnable listener : this.hotkeyChangeListeners) {
            listener.run();
        }
    }

    @Override
    @Nullable
    public IConfigInfoProvider getHoverInfoProvider()
    {
        return this.hoverInfoProvider;
    }

    @Override
    public void clearOptions() {
        this.hotkeyChangeListeners.clear();
    }

    @Override
    public ButtonPressDirtyListenerSimple getButtonPressListener()
    {
        return this.dirtyListener;
    }

    public static class ConfigOptionWrapper {
        private final Type type;
        @Nullable
        private final IConfigBase config;
        @Nullable
        private final String label;

        public ConfigOptionWrapper(IConfigBase config) {
            this.type = Type.CONFIG;
            this.config = config;
            this.label = null;
        }

        public ConfigOptionWrapper(String label) {
            this.type = Type.LABEL;
            this.config = null;
            this.label = label;
        }

        public static List<ConfigOptionWrapper> createFor(Collection<? extends IConfigBase> configs) {
            ImmutableList.Builder<ConfigOptionWrapper> builder = ImmutableList.builder();

            for (IConfigBase config : configs) {
                builder.add(new ConfigOptionWrapper(config));
            }

            return builder.build();
        }

        public Type getType() {
            return this.type;
        }

        @Nullable
        public IConfigBase getConfig() {
            return this.config;
        }

        @Nullable
        public String getLabel() {
            return this.label;
        }

        public enum Type {
            CONFIG,
            LABEL
        }
    }
}
