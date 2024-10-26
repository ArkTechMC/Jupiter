package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.iafenvoy.jupiter.config.interfaces.IRangeConfigBase;
import com.mojang.serialization.Codec;

public class ConfigDouble extends ConfigBase<Double> implements IRangeConfigBase<Double> {
    private final double defaultValue, minValue, maxValue;
    private double value;
    private boolean useSlider = false;

    public ConfigDouble(String nameKey, double defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public ConfigDouble(String nameKey, double defaultValue, double minValue, double maxValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ConfigDouble slider() {
        if (Integer.MIN_VALUE < this.minValue && this.maxValue < Integer.MAX_VALUE)
            this.useSlider = true;
        return this;
    }

    @Override
    public ConfigType<Double> getType() {
        return ConfigType.DOUBLE;
    }

    @Override
    public IConfigBase<Double> newInstance() {
        return new ConfigDouble(this.nameKey, this.defaultValue, this.minValue, this.maxValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    @Override
    public Double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<Double> getCodec() {
        return Codec.doubleRange(this.minValue, this.maxValue);
    }

    @Override
    public Double getMinValue() {
        return this.minValue;
    }

    @Override
    public Double getMaxValue() {
        return this.maxValue;
    }

    @Override
    public boolean useSlider() {
        return this.useSlider;
    }
}
