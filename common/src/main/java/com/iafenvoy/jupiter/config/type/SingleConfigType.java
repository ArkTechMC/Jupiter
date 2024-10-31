package com.iafenvoy.jupiter.config.type;

public class SingleConfigType<T> extends ConfigType<T> {
    public SingleConfigType() {
        super(Type.SINGLE);
    }
}
