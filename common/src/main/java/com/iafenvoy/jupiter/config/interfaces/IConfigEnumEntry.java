package com.iafenvoy.jupiter.config.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IConfigEnumEntry {
    String getName();

    @NotNull
    IConfigEnumEntry getByName(String name);
}
