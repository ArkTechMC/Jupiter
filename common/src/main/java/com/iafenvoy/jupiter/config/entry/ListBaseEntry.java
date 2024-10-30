package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public abstract class ListBaseEntry<T> extends BaseEntry<List<T>> {
    public ListBaseEntry(String nameKey, List<T> defaultValue) {
        super(nameKey, defaultValue);
    }

    public abstract Codec<T> getValueCodec();

    public abstract IConfigEntry<T> newSingleInstance(T value, int index, Runnable reload);

    public abstract T newValue();

    @Override
    public Codec<List<T>> getCodec() {
        return this.getValueCodec().listOf();
    }

    @Override
    public void setValue(List<T> value) {
        super.setValue(new ArrayList<>(value));
    }

    @Override
    protected List<T> copyDefaultData() {
        return new ArrayList<>(this.defaultValue);
    }
}
