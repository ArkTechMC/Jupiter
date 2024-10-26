package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.iafenvoy.jupiter.config.interfaces.IRangeConfigBase;
import com.mojang.serialization.Codec;

public class ConfigInteger extends ConfigBase<Integer> implements IRangeConfigBase<Integer> {
    private final int defaultValue, minValue, maxValue;
    private int value;
    private boolean useSlider = false;

    public ConfigInteger(String nameKey, int defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public ConfigInteger slider() {
        if (Integer.MIN_VALUE < this.minValue && this.maxValue < Integer.MAX_VALUE)
            this.useSlider = true;
        return this;
    }

    @Override
    public ConfigType<Integer> getType() {
        return ConfigType.INTEGER;
    }

    @Override
    public IConfigBase<Integer> newInstance() {
        return new ConfigInteger(this.nameKey, this.defaultValue, this.minValue, this.maxValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public Integer getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<Integer> getCodec() {
        return Codec.intRange(this.minValue, this.maxValue);
    }

    @Override
    public Integer getMinValue() {
        return this.minValue;
    }

    @Override
    public Integer getMaxValue() {
        return this.maxValue;
    }

    @Override
    public boolean useSlider() {
        return this.useSlider;
    }
}
