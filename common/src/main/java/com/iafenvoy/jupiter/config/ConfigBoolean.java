package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

public class ConfigBoolean extends ConfigBase<Boolean> {
    public ConfigBoolean(String nameKey, boolean defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<Boolean> getType() {
        return ConfigType.BOOLEAN;
    }

    @Override
    public IConfigBase<Boolean> newInstance() {
        return new ConfigBoolean(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }
}
