package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

public abstract class EntryBaseEntry<T> extends BaseEntry<Map.Entry<String, T>> {
    @ApiStatus.Internal
    public EntryBaseEntry(String nameKey, Map.Entry<String, T> defaultValue) {
        super(nameKey, defaultValue);
    }

    public abstract IConfigEntry<T> newValueInstance();
}
