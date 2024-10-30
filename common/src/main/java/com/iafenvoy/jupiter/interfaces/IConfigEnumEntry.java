package com.iafenvoy.jupiter.interfaces;

import org.jetbrains.annotations.NotNull;

public interface IConfigEnumEntry {
    String getName();

    @NotNull
    IConfigEnumEntry getByName(String name);

    IConfigEnumEntry cycle(boolean clockWise);
}
