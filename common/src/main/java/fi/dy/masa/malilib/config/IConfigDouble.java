package fi.dy.masa.malilib.config;

public interface IConfigDouble extends IConfigValue, IConfigSlider {
    double getDoubleValue();

    void setDoubleValue(double value);

    double getDefaultDoubleValue();

    double getMinDoubleValue();

    double getMaxDoubleValue();
}
