package com.iafenvoy.jupiter.interfaces;

import net.minecraft.util.Identifier;

public interface IConfigHandler {
    Identifier getConfigId();

    String getTitleNameKey();

    default void onConfigsChanged() {
        this.save();
        this.load();
    }

    void load();

    void save();

    void init();
}
