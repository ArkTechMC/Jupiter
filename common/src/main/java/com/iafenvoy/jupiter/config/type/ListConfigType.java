package com.iafenvoy.jupiter.config.type;

import java.util.List;

public class ListConfigType<T> extends ConfigType<List<T>> {
    private final ConfigType<T> singleType;

    public ListConfigType(ConfigType<T> singleType) {
        super(EntryType.LIST);
        this.singleType = singleType;
    }

    public ConfigType<T> getSingleType() {
        return singleType;
    }
}
