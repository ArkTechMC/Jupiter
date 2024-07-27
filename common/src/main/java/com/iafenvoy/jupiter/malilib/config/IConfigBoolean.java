package com.iafenvoy.jupiter.malilib.config;

public interface IConfigBoolean extends IConfigValue {
    boolean getBooleanValue();

    void setBooleanValue(boolean value);

    boolean getDefaultBooleanValue();

    default void toggleBooleanValue() {
        this.setBooleanValue(!this.getBooleanValue());
    }
}
