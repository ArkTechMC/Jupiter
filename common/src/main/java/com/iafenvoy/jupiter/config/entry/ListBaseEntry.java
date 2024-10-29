package com.iafenvoy.jupiter.config.entry;

import com.mojang.serialization.Codec;

import java.util.List;

public abstract class ListBaseEntry<T> extends BaseEntry<List<T>> {
    public ListBaseEntry(String nameKey, List<T> defaultValue) {
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
