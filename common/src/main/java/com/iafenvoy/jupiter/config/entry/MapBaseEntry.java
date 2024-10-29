package com.iafenvoy.jupiter.config.entry;

import com.mojang.serialization.Codec;

import java.util.Map;

public abstract class MapBaseEntry<T> extends BaseEntry<Map<String, T>> {
    public MapBaseEntry(String nameKey, Map<String, T> defaultValue) {
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
