package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.HashMap;
import java.util.Map;

public abstract class MapBaseEntry<T> extends BaseEntry<Map<String, T>> {
    public MapBaseEntry(String nameKey, Map<String, T> defaultValue) {
        super(nameKey, defaultValue);
    }

    public abstract Codec<T> getValueCodec();

    public abstract IConfigEntry<Map.Entry<String, T>> newSingleInstance(T value, String key, Runnable reload);

    public abstract T newValue();

    @Override
    public Codec<Map<String, T>> getCodec() {
        return Codec.unboundedMap(Codec.STRING, this.getValueCodec());
    }

    @Override
    public void setValue(Map<String, T> value) {
        super.setValue(new HashMap<>(value));
    }

    @Override
    protected Map<String, T> copyDefaultData() {
        return new HashMap<>(this.defaultValue);
    }
}
