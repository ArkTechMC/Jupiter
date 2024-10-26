package com.iafenvoy.jupiter.config.interfaces;

public interface IRangeConfigBase<T extends Number> extends IConfigBase<T> {
    T getMinValue();

    T getMaxValue();

    boolean useSlider();
}
