package com.iafenvoy.jupiter.malilib.config;

import com.iafenvoy.jupiter.malilib.interfaces.IValueChangeCallback;

public interface IConfigNotifiable<T extends IConfigBase> {
    void onValueChanged();

    IConfigBase setValueChangeCallback(IValueChangeCallback<T> callback);
}
