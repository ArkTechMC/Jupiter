package com.iafenvoy.jupiter.config.type;

public class ConfigType<T> {
    private final EntryType type;

    public ConfigType(EntryType type) {
        this.type = type;
    }

    public EntryType getType() {
        return type;
    }

    public enum EntryType {
        SINGLE, LIST, MAP, DUMMY
    }
}
