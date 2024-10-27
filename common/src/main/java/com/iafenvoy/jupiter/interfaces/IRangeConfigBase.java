package com.iafenvoy.jupiter.interfaces;

public interface IRangeConfigBase<T extends Number> extends IConfigBase<T>, ITextFieldConfig {
    T getMinValue();

    T getMaxValue();

    boolean useSlider();
}
