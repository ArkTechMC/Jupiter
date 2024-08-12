package com.iafenvoy.jupiter.malilib.config;

public interface IConfigResettable {
    /**
     * Returns true if the value has been changed from the default value
     *
     */
    boolean isModified();

    /**
     * Resets the value back to the default value
     */
    void resetToDefault();
}
