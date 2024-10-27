package com.iafenvoy.jupiter.config;

import com.mojang.serialization.Codec;

import java.util.List;

public abstract class ConfigListBase<T> extends ConfigBase<List<T>> {
    public ConfigListBase(String nameKey, List<T> defaultValue) {
        super(nameKey, defaultValue);
    }

    public abstract Codec<T> getValueCodec();

    @Override
    public Codec<List<T>> getCodec() {
        return this.getValueCodec().listOf();
    }

    @Override
    public void reset() {
        this.value = List.copyOf(this.defaultValue);
    }
}
