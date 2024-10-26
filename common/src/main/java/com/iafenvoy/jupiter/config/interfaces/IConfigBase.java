package com.iafenvoy.jupiter.config.interfaces;

import com.iafenvoy.jupiter.config.ConfigType;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

public interface IConfigBase<T> {
    String COMMENT_SUFFIX = ".comment";

    ConfigType<T> getType();

    default String getJsonKey() {
        return this.getNameKey();
    }

    String getNameKey();

    @Nullable
    String getComment();

    IConfigBase<T> newInstance();

    default void copyFrom(IConfigBase<T> another) {
        this.setValue(another.getValue());
    }

    T getValue();

    T getDefaultValue();

    void setValue(T value);

    Codec<T> getCodec();

    default Codec<IConfigBase<T>> getConfigCodec() {
        return this.getCodec().xmap(v -> {
            IConfigBase<T> i = this.newInstance();
            i.setValue(v);
            return i;
        }, IConfigBase::getValue);
    }
}
