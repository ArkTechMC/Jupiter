package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.IRangeConfigEntry;
import com.mojang.serialization.Codec;

public class DoubleEntry extends BaseEntry<Double> implements IRangeConfigEntry<Double> {
    private final double minValue, maxValue;
    private boolean useSlider = false;

    public DoubleEntry(String nameKey, double defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public DoubleEntry(String nameKey, double defaultValue, double minValue, double maxValue) {
        super(nameKey, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleEntry slider() {
        if (Integer.MIN_VALUE < this.minValue && this.maxValue < Integer.MAX_VALUE)
            this.useSlider = true;
        return this;
    }

    @Override
    public ConfigType<Double> getType() {
        return ConfigType.DOUBLE;
    }

    @Override
    public IConfigEntry<Double> newInstance() {
        return new DoubleEntry(this.nameKey, this.defaultValue, this.minValue, this.maxValue).comment(this.commentKey).json(this.jsonKey);
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
