package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigValue;
import com.mojang.serialization.Codec;

public class ConfigString extends ConfigBase<ConfigString> implements IConfigValue {
    private final String defaultValue;
    private String value;
    private String previousValue;

    public ConfigString(String nameKey, String defaultValue) {
        this(nameKey, defaultValue, nameKey + COMMENT_SUFFIX);
    }

    public ConfigString(String nameKey, String defaultValue, String comment) {
        super(ConfigType.STRING, nameKey, comment);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.previousValue = defaultValue;
    }

    @Override
    public String getStringValue() {
        return this.value;
    }

    @Override
    public String getDefaultStringValue() {
        return this.defaultValue;
    }

    public String getOldStringValue() {
        return this.previousValue;
    }

    @Override
    public void setValueFromString(String value) {
        this.previousValue = this.value;
        this.value = value;

        if (!this.previousValue.equals(this.value)) {
            this.onValueChanged();
        }
    }

    @Override
    public void resetToDefault() {
        this.setValueFromString(this.defaultValue);
    }

    @Override
    public boolean isModified() {
        return !this.value.equals(this.defaultValue);
    }

    @Override
    public boolean isModified(String newValue) {
        return !this.defaultValue.equals(newValue);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.value = element.getAsString();
                this.previousValue = this.value;
            } else {
                MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element);
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(this.value);
    }

    @Override
    public IConfigBase copy() {
        return new ConfigString(this.getNameKey(), this.defaultValue, this.getCommentKey());
    }

    @Override
    public Codec<?> getCodec() {
        return Codec.STRING.orElse(this.defaultValue);
    }
}
