package fi.dy.masa.malilib.config;

public interface IConfigInteger extends IConfigValue, IConfigSlider {
    int getIntegerValue();

    void setIntegerValue(int value);

    int getDefaultIntegerValue();

    int getMinIntegerValue();

    int getMaxIntegerValue();
}
