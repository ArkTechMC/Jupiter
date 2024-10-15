package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigInteger;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.MathHelper;

public class ConfigInteger extends ConfigBase<ConfigInteger> implements IConfigInteger {
    protected final int minValue;
    protected final int maxValue;
    protected final int defaultValue;
    protected int value;
    private boolean useSlider;

    public ConfigInteger(String nameKey, int defaultValue) {
        this(nameKey, defaultValue, nameKey + COMMENT_SUFFIX);
    }

    public ConfigInteger(String nameKey, int defaultValue, String comment) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, comment);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue) {
        this(nameKey, defaultValue, minValue, maxValue, false, nameKey + COMMENT_SUFFIX);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue, String comment) {
        this(nameKey, defaultValue, minValue, maxValue, false, comment);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        this(nameKey, defaultValue, minValue, maxValue, useSlider, nameKey + COMMENT_SUFFIX);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue, boolean useSlider, String comment) {
        super(ConfigType.INTEGER, nameKey, comment);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.useSlider = useSlider;
    }

    @Override
    public boolean shouldUseSlider() {
        return this.useSlider;
    }

    @Override
    public void toggleUseSlider() {
        this.useSlider = !this.useSlider;
    }

    @Override
    public int getIntegerValue() {
        return this.value;
    }

    @Override
    public void setIntegerValue(int value) {
        int oldValue = this.value;
        this.value = this.getClampedValue(value);

        if (oldValue != this.value) {
            this.onValueChanged();
        }
    }

    @Override
    public int getDefaultIntegerValue() {
        return this.defaultValue;
    }

    @Override
    public int getMinIntegerValue() {
        return this.minValue;
    }

    @Override
    public int getMaxIntegerValue() {
        return this.maxValue;
    }

    protected int getClampedValue(int value) {
        return MathHelper.clamp(value, this.minValue, this.maxValue);
    }

    @Override
    public boolean isModified() {
        return this.value != this.defaultValue;
    }

    @Override
    public boolean isModified(String newValue) {
        try {
            return Integer.parseInt(newValue) != this.defaultValue;
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to get integer", e);
        }

        return true;
    }

    @Override
    public void resetToDefault() {
        this.setIntegerValue(this.defaultValue);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    @Override
    public String getDefaultStringValue() {
        return String.valueOf(this.defaultValue);
    }

    @Override
    public void setValueFromString(String value) {
        try {
            this.setIntegerValue(Integer.parseInt(value));
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for {} from the string '{}'", this.getNameKey(), value, e);
        }
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.value = this.getClampedValue(element.getAsInt());
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
        return new ConfigInteger(this.getNameKey(), this.defaultValue, this.minValue, this.maxValue, this.useSlider, this.getPrettyNameKey());
    }

    @Override
    public Codec<?> getCodec() {
        return Codec.intRange(this.minValue, this.maxValue).orElse(this.defaultValue);
    }
}
