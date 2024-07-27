package com.iafenvoy.jupiter.malilib.config;

public interface IConfigSlider extends IConfigValue {
    default boolean shouldUseSlider() {
        return false;
    }

    default void toggleUseSlider() {
    }
}
