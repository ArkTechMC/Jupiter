package com.iafenvoy.jupiter.interfaces;

public interface IRangeConfigEntry<T extends Number> extends IConfigEntry<T>, ITextFieldConfig {
    T getMinValue();

    T getMaxValue();

    boolean useSlider();
}
