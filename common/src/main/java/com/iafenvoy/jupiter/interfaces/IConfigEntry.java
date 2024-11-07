package com.iafenvoy.jupiter.interfaces;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.type.ConfigType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.client.resource.language.I18n;

import java.util.function.Consumer;

public interface IConfigEntry<T> {
    ConfigType<T> getType();

    default String getJsonKey() {
        return this.getNameKey();
    }

    String getNameKey();

    IConfigEntry<T> newInstance();

    default String getPrettyName() {
        return I18n.translate(this.getNameKey());
    }

    void registerCallback(Consumer<T> callback);

    T getValue();

    T getDefaultValue();

    void setValue(T value);

    Codec<T> getCodec();

    default <R> DataResult<R> encode(DynamicOps<R> ops) {
        return this.getCodec().encodeStart(ops, this.getValue());
    }

    default <R> void decode(DynamicOps<R> ops, R input) {
        this.setValue(this.getCodec().parse(ops, input).getOrThrow(true, Jupiter.LOGGER::error));
    }

    void reset();
}
