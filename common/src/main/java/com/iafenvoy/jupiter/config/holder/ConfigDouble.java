package com.iafenvoy.jupiter.config.holder;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.iafenvoy.jupiter.interfaces.IRangeConfigBase;
import com.mojang.serialization.Codec;

public class ConfigDouble extends ConfigBase<Double> implements IRangeConfigBase<Double> {
    private final double minValue, maxValue;
    private boolean useSlider = false;

    public ConfigDouble(String nameKey, double defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public ConfigDouble(String nameKey, double defaultValue, double minValue, double maxValue) {
        super(nameKey, defaultValue);
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

    @Override
    public String valueAsString() {
        return String.valueOf(this.getValue());
    }

    @Override
    public void setValueFromString(String s) {
        this.setValue(Double.parseDouble(s));
    }
}
