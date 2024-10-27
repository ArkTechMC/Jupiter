package com.iafenvoy.jupiter.config.holder;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.iafenvoy.jupiter.interfaces.IRangeConfigBase;
import com.mojang.serialization.Codec;

public class ConfigInteger extends ConfigBase<Integer> implements IRangeConfigBase<Integer> {
    private final int minValue, maxValue;
    private boolean useSlider = false;

    public ConfigInteger(String nameKey, int defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public ConfigInteger(String nameKey, int defaultValue, int minValue, int maxValue) {
        super(nameKey, defaultValue);
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

    @Override
    public String valueAsString() {
        return String.valueOf(this.getValue());
    }

    @Override
    public void setValueFromString(String s) {
        this.setValue(Integer.parseInt(s));
    }
}
