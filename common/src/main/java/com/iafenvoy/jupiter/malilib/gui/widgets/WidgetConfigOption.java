package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.malilib.config.*;
import com.iafenvoy.jupiter.malilib.config.gui.*;
import com.iafenvoy.jupiter.malilib.config.gui.ConfigOptionListenerResetConfig.ConfigResetterButton;
import com.iafenvoy.jupiter.malilib.config.gui.ConfigOptionListenerResetConfig.ConfigResetterTextField;
import com.iafenvoy.jupiter.malilib.gui.GuiBase;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase;
import com.iafenvoy.jupiter.malilib.gui.GuiTextFieldGeneric;
import com.iafenvoy.jupiter.malilib.gui.MaLiLibIcons;
import com.iafenvoy.jupiter.malilib.gui.button.*;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigGui;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IConfigInfoProvider;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IGuiIcon;
import com.iafenvoy.jupiter.malilib.gui.interfaces.ISliderCallback;
import com.iafenvoy.jupiter.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public class WidgetConfigOption extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {
    protected final GuiConfigsBase.ConfigOptionWrapper wrapper;
    protected final IConfigGui host;

    @Nullable
    protected ImmutableList<String> initialStringList;
    protected int colorDisplayPosX;
    private boolean initialBoolean;

    public WidgetConfigOption(int x, int y, int width, int height, int labelWidth, int configWidth,
                              GuiConfigsBase.ConfigOptionWrapper wrapper, int listIndex, IConfigGui host, WidgetListConfigOptionsBase<?, ?> parent) {
        super(x, y, width, height, parent, wrapper, listIndex);
        this.wrapper = wrapper;
        this.host = host;

        if (wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
            IConfigBase config = wrapper.getConfig();

            if (config instanceof IStringRepresentable configStr) {
                this.initialStringValue = configStr.getStringValue();
                this.lastAppliedValue = configStr.getStringValue();
            } else {
                this.initialStringValue = null;
                this.lastAppliedValue = null;

                if (config instanceof IConfigStringList) {
                    this.initialStringList = ImmutableList.copyOf(((IConfigStringList) config).getStrings());
                }
            }

            this.addConfigOption(x, y, this.zLevel, labelWidth, configWidth, config);
        } else {
            this.initialStringValue = null;
            this.lastAppliedValue = null;

            this.addLabel(x, y + 7, labelWidth, 8, 0xFFFFFFFF, wrapper.getLabel());
        }
    }

    protected void addConfigOption(int x, int y, float zLevel, int labelWidth, int configWidth, IConfigBase config) {
        ConfigType type = config.getType();

        y += 1;
        int configHeight = 20;

        this.addLabel(x, y + 7, labelWidth, 8, 0xFFFFFFFF, config.getConfigGuiDisplayName());

        String comment;
        IConfigInfoProvider infoProvider = this.host.getHoverInfoProvider();

        if (infoProvider != null) {
            comment = infoProvider.getHoverInfo(config);
        } else {
            comment = config.getComment();
        }

        if (comment != null) {
            this.addConfigComment(x, y + 5, labelWidth, 12, comment);
        }

        x += labelWidth + 10;

        if (type == ConfigType.BOOLEAN) {
            ConfigButtonBoolean optionButton = new ConfigButtonBoolean(x, y, configWidth, configHeight, (IConfigBoolean) config);
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
        } else if (type == ConfigType.OPTION_LIST) {
            ConfigButtonOptionList optionButton = new ConfigButtonOptionList(x, y, configWidth, configHeight, (IConfigOptionList) config);
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
        } else if (type == ConfigType.STRING_LIST) {
            ConfigButtonStringList optionButton = new ConfigButtonStringList(x, y, configWidth, configHeight, (IConfigStringList) config, this.host, this.host.getDialogHandler());
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
        } else if (type == ConfigType.COLOR_LIST) {
            ConfigButtonColorList optionButton = new ConfigButtonColorList(x, y, configWidth, configHeight, (IConfigColorList) config, this.host, this.host.getDialogHandler());
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
        } else if (type == ConfigType.STRING ||
                type == ConfigType.COLOR ||
                type == ConfigType.INTEGER ||
                type == ConfigType.DOUBLE) {
            int resetX = x + configWidth + 2;

            if (type == ConfigType.COLOR) {
                configWidth -= 22; // adjust the width to match other configs due to the color display
                this.colorDisplayPosX = x + configWidth + 2;
                this.addWidget(new WidgetColorIndicator(this.colorDisplayPosX, y + 1, 19, 19, (IConfigInteger) config));
            } else if (type == ConfigType.INTEGER || type == ConfigType.DOUBLE) {
                configWidth -= 18;
                this.colorDisplayPosX = x + configWidth + 2;
            }

            if ((type == ConfigType.INTEGER || type == ConfigType.DOUBLE) &&
                    config instanceof IConfigSlider && ((IConfigSlider) config).shouldUseSlider()) {
                this.addConfigSliderEntry(x, y, resetX, configWidth, configHeight, (IConfigSlider) config);
            } else {
                this.addConfigTextFieldEntry(x, y, resetX, configWidth, configHeight, (IConfigValue) config);
            }

            if (type != ConfigType.COLOR && config instanceof IConfigSlider) {
                IGuiIcon icon = ((IConfigSlider) config).shouldUseSlider() ? MaLiLibIcons.BTN_TXTFIELD : MaLiLibIcons.BTN_SLIDER;
                ButtonGeneric toggleBtn = new ButtonGeneric(this.colorDisplayPosX, y + 2, icon);
                this.addButton(toggleBtn, new ListenerSliderToggle((IConfigSlider) config));
            }
        }
    }

    @Override
    public boolean wasConfigModified() {
        if (this.wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
            IConfigBase config = this.wrapper.getConfig();
            boolean modified = false;

            if (config instanceof IStringRepresentable) {
                if (this.textField != null) {
                    modified |= !this.initialStringValue.equals(this.textField.textField().getText());
                }

                return modified || !this.initialStringValue.equals(((IStringRepresentable) config).getStringValue());
            } else if (this.initialStringList != null && config instanceof IConfigStringList) {
                return !this.initialStringList.equals(((IConfigStringList) config).getStrings());
            }
        }

        return false;
    }

    @Override
    public void applyNewValueToConfig() {
        if (this.wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG &&
                this.wrapper.getConfig() instanceof IStringRepresentable config) {
            if (this.textField != null && this.hasPendingModifications()) {
                config.setValueFromString(this.textField.textField().getText());
            }

            this.lastAppliedValue = config.getStringValue();
        }
    }

    protected void addConfigComment(int x, int y, int width, int height, String comment) {
        this.addWidget(new WidgetHoverInfo(x, y, width, height, comment));
    }

    protected void addConfigButtonEntry(int xReset, int yReset, IConfigResettable config, ButtonBase optionButton) {
        ButtonGeneric resetButton = this.createResetButton(xReset, yReset, config);
        ConfigOptionChangeListenerButton listenerChange = new ConfigOptionChangeListenerButton(config, resetButton, null);
        ConfigOptionListenerResetConfig listenerReset = new ConfigOptionListenerResetConfig(config, new ConfigResetterButton(optionButton), resetButton, null);

        this.addButton(optionButton, listenerChange);
        this.addButton(resetButton, listenerReset);
    }

    protected void addConfigTextFieldEntry(int x, int y, int resetX, int configWidth, int configHeight, IConfigValue config) {
        GuiTextFieldGeneric field = this.createTextField(x, y + 1, configWidth - 4, configHeight - 3);
        field.setMaxLength(this.maxTextfieldTextLength);
        field.setText(config.getStringValue());

        ButtonGeneric resetButton = this.createResetButton(resetX, y, config);
        ConfigOptionChangeListenerTextField listenerChange = new ConfigOptionChangeListenerTextField(config, field, resetButton);
        ConfigOptionListenerResetConfig listenerReset = new ConfigOptionListenerResetConfig(config, new ConfigResetterTextField(config, field), resetButton, null);

        this.addTextField(field, listenerChange);
        this.addButton(resetButton, listenerReset);
    }

    protected void addConfigSliderEntry(int x, int y, int resetX, int configWidth, int configHeight, IConfigSlider config) {
        ButtonGeneric resetButton = this.createResetButton(resetX, y, config);
        ISliderCallback callback;

        if (config instanceof IConfigDouble) {
            callback = new SliderCallbackDouble((IConfigDouble) config, resetButton);
        } else if (config instanceof IConfigInteger) {
            callback = new SliderCallbackInteger((IConfigInteger) config, resetButton);
        } else {
            return;
        }

        WidgetSlider slider = new WidgetSlider(x, y, configWidth, configHeight, callback);
        ConfigOptionListenerResetConfig listenerReset = new ConfigOptionListenerResetConfig(config, null, resetButton, null);

        this.addWidget(slider);
        this.addButton(resetButton, listenerReset);
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        RenderUtils.color(1f, 1f, 1f, 1f);

        this.drawSubWidgets(mouseX, mouseY, drawContext);

        if (this.wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
            this.drawTextFields(mouseX, mouseY, drawContext);
            super.render(mouseX, mouseY, selected, drawContext);
        }
    }

    public static class ListenerSliderToggle implements IButtonActionListener {
        protected final IConfigSlider config;

        public ListenerSliderToggle(IConfigSlider config) {
            this.config = config;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            this.config.toggleUseSlider();

            Screen gui = MinecraftClient.getInstance().currentScreen;

            if (gui instanceof GuiBase) {
                ((GuiBase) gui).initGui();
            }
        }
    }
}
