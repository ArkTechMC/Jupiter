package com.iafenvoy.jupiter.interfaces;

import com.mojang.serialization.Codec;

public interface ICollectionConfig<T, K> {
    Codec<T> getValueCodec();

    IConfigEntry<T> newSingleInstance(T value, K index, Runnable reload);

    T newValue();
}
