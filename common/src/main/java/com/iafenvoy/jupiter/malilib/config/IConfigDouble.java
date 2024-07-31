package com.iafenvoy.jupiter.malilib.config;

public interface IConfigDouble extends IConfigValue, IConfigSlider {
    double getDoubleValue();

    float getFloatValue();

    void setDoubleValue(double value);

    double getDefaultDoubleValue();

    double getMinDoubleValue();

    double getMaxDoubleValue();
}
