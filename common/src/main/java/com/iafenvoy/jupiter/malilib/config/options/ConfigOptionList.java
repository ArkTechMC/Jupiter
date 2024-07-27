package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigOptionList;
import com.iafenvoy.jupiter.malilib.config.IConfigOptionListEntry;
import com.iafenvoy.jupiter.malilib.config.IStringRepresentable;

public class ConfigOptionList extends ConfigBase<ConfigOptionList> implements IConfigOptionList, IStringRepresentable {
    private final IConfigOptionListEntry defaultValue;
    private IConfigOptionListEntry value;

    public ConfigOptionList(String name, IConfigOptionListEntry defaultValue, String comment) {
        this(name, defaultValue, comment, name);
    }

    public ConfigOptionList(String name, IConfigOptionListEntry defaultValue, String comment, String prettyName) {
        super(ConfigType.OPTION_LIST, name, comment, prettyName);

        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public IConfigOptionListEntry getOptionListValue() {
        return this.value;
    }

    @Override
    public void setOptionListValue(IConfigOptionListEntry value) {
        IConfigOptionListEntry oldValue = this.value;
        this.value = value;

        if (oldValue != this.value) {
            this.onValueChanged();
        }
    }

    @Override
    public IConfigOptionListEntry getDefaultOptionListValue() {
        return this.defaultValue;
    }

    @Override
    public boolean isModified() {
        return this.value != this.defaultValue;
    }

    @Override
    public boolean isModified(String newValue) {
        try {
            return this.value.fromString(newValue) != this.defaultValue;
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public void resetToDefault() {
        this.setOptionListValue(this.defaultValue);
    }

    @Override
    public String getStringValue() {
        return this.value.getStringValue();
    }

    @Override
    public String getDefaultStringValue() {
        return this.defaultValue.getStringValue();
    }

    @Override
    public void setValueFromString(String value) {
        this.value = this.value.fromString(value);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.setValueFromString(element.getAsString());
            } else {
                MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(this.getStringValue());
    }
}
