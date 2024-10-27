package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

import java.util.Map;

public class ConfigMapString extends ConfigMapBase<String> {
    public ConfigMapString(String nameKey, Map<String, String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<String> getValueCodec() {
        return Codec.STRING;
    }

    @Override
    public ConfigType<Map<String, String>> getType() {
        return ConfigType.MAP_STRING;
    }

    @Override
    public IConfigBase<Map<String, String>> newInstance() {
        return new ConfigMapString(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }
}
