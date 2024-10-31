package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.IRangeConfigEntry;
import com.mojang.serialization.Codec;

public class IntegerEntry extends BaseEntry<Integer> implements IRangeConfigEntry<Integer> {
    private final int minValue, maxValue;
    private boolean useSlider = false;

    public IntegerEntry(String nameKey, int defaultValue) {
        this(nameKey, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerEntry(String nameKey, int defaultValue, int minValue, int maxValue) {
        super(nameKey, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public IntegerEntry slider() {
        if (Integer.MIN_VALUE < this.minValue && this.maxValue < Integer.MAX_VALUE)
            this.useSlider = true;
        return this;
    }

    @Override
    public ConfigType<Integer> getType() {
        return ConfigTypes.INTEGER;
    }

    @Override
    public IConfigEntry<Integer> newInstance() {
        return new IntegerEntry(this.nameKey, this.defaultValue, this.minValue, this.maxValue).visible(this.visible).json(this.jsonKey);
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
        int d = Integer.parseInt(s);
        if (d < this.minValue || d > this.maxValue) throw new IllegalArgumentException();
        this.setValue(d);
    }
}
