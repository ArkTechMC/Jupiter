package com.iafenvoy.jupiter.malilib.config.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigDouble;
import net.minecraft.util.math.MathHelper;

public class ConfigDouble extends ConfigBase<ConfigDouble> implements IConfigDouble {
    private final double minValue;
    private final double maxValue;
    private final double defaultValue;
    private double value;
    private boolean useSlider;

    public ConfigDouble(String nameKey, double defaultValue, String comment) {
        this(nameKey, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE, comment);
    }

    public ConfigDouble(String nameKey, double defaultValue, double minValue, double maxValue, String comment) {
        this(nameKey, defaultValue, minValue, maxValue, false, comment);
    }

    public ConfigDouble(String nameKey, double defaultValue, double minValue, double maxValue, boolean useSlider, String comment) {
        super(ConfigType.DOUBLE, nameKey, comment);

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
    public double getDoubleValue() {
        return this.value;
    }

    @Override
    public void setDoubleValue(double value) {
        double oldValue = this.value;
        this.value = this.getClampedValue(value);

        if (oldValue != this.value) {
            this.onValueChanged();
        }
    }

    @Override
    public double getDefaultDoubleValue() {
        return this.defaultValue;
    }

    @Override
    public double getMinDoubleValue() {
        return this.minValue;
    }

    @Override
    public double getMaxDoubleValue() {
        return this.maxValue;
    }

    protected double getClampedValue(double value) {
        return MathHelper.clamp(value, this.minValue, this.maxValue);
    }

    @Override
    public boolean isModified() {
        return this.value != this.defaultValue;
    }

    @Override
    public boolean isModified(String newValue) {
        try {
            return Double.parseDouble(newValue) != this.defaultValue;
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public void resetToDefault() {
        this.setDoubleValue(this.defaultValue);
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
            this.setDoubleValue(Double.parseDouble(value));
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for {} from the string '{}'", this.getNameKey(), value, e);
        }
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonPrimitive()) {
                this.value = this.getClampedValue(element.getAsDouble());
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
}
