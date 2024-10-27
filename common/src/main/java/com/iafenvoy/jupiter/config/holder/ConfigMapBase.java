package com.iafenvoy.jupiter.config.holder;

import com.mojang.serialization.Codec;

import java.util.Map;

public abstract class ConfigMapBase<T> extends ConfigBase<Map<String, T>> {
    public ConfigMapBase(String nameKey, Map<String, T> defaultValue) {
        super(nameKey, defaultValue);
    }

    public abstract Codec<T> getValueCodec();

    @Override
    public Codec<Map<String, T>> getCodec() {
        return Codec.unboundedMap(Codec.STRING, this.getValueCodec());
    }

    @Override
    public void reset() {
        this.value = Map.copyOf(this.defaultValue);
    }
}
