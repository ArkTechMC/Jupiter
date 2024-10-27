package com.iafenvoy.jupiter.config.holder;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

//TODO: Ensure this can work
@ApiStatus.Internal
public final class ConfigNull implements IConfigBase<Void> {
    @Override
    public ConfigType<Void> getType() {
        return ConfigType.VOID;
    }

    @Override
    public String getNameKey() {
        return "";
    }

    @Override
    public @Nullable String getComment() {
        return null;
    }

    @Override
    public IConfigBase<Void> newInstance() {
        return new ConfigNull();
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public Void getDefaultValue() {
        return null;
    }

    @Override
    public void setValue(Void value) {
    }

    @Override
    public Codec<Void> getCodec() {
        return Codec.unit(null);
    }

    @Override
    public void reset() {
    }
}
