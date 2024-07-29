package com.iafenvoy.jupiter.malilib.interfaces;

import com.iafenvoy.jupiter.malilib.config.IConfigBase;

public interface IValueChangeCallback<T extends IConfigBase> {
    /**
     * Called when (= after) the config's value is changed
     */
    void onValueChanged(T config);
}
