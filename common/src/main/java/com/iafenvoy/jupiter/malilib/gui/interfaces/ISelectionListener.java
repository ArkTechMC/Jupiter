package com.iafenvoy.jupiter.malilib.gui.interfaces;

import org.jetbrains.annotations.Nullable;

public interface ISelectionListener<T> {
    void onSelectionChange(@Nullable T entry);
}
