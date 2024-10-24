package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.util.Color4f;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.mojang.serialization.Codec;

public class ConfigColor extends ConfigInteger {
    private Color4f color;

    public ConfigColor(String nameKey, String defaultValue) {
        this(nameKey, defaultValue, nameKey + COMMENT_SUFFIX);
    }

    public ConfigColor(String nameKey, String defaultValue, String comment) {
        super(nameKey, StringUtils.getColor(defaultValue, 0), comment);
        this.color = Color4f.fromColor(this.getIntegerValue());
    }

    @Override
    public ConfigType getType() {
        return ConfigType.COLOR;
    }

    public Color4f getColor() {
        return this.color;
    }

    @Override
    public String getStringValue() {
        return String.format("#%08X", this.getIntegerValue());
    }

    @Override
    public String getDefaultStringValue() {
        return String.format("#%08X", this.getDefaultIntegerValue());
    }

    @Override
    public void setValueFromString(String value) {
        this.setIntegerValue(StringUtils.getColor(value, 0));
    }

    @Override
    public void setIntegerValue(int value) {
        this.color = Color4f.fromColor(value);

        super.setIntegerValue(value); // This also calls the callback, if set
    }

    @Override
    public boolean isModified(String newValue) {
        try {
            return StringUtils.getColor(newValue, 0) != this.getDefaultIntegerValue();
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to get color", e);
        }

        return true;
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.value = this.getClampedValue(StringUtils.getColor(element.getAsString(), 0));
                this.color = Color4f.fromColor(this.value);
            } else {
                MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element);
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        return new JsonPrimitive(this.getStringValue());
    }

    @Override
    public Codec<?> getCodec() {
        return Color4f.CODEC.orElse(this.getColor());
    }
}
