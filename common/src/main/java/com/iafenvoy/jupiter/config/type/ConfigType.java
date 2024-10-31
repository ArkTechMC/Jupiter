package com.iafenvoy.jupiter.config.type;

public class ConfigType<T> {
    private final Type type;

    public ConfigType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        SINGLE, LIST, MAP, DUMMY
    }
}
