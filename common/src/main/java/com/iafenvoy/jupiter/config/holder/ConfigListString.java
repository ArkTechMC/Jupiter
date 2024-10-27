package com.iafenvoy.jupiter.config.holder;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

import java.util.List;

public class ConfigListString extends ConfigListBase<String> {
    public ConfigListString(String nameKey, List<String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<String> getValueCodec() {
        return Codec.STRING;
    }

    @Override
    public ConfigType<List<String>> getType() {
        return ConfigType.LIST_STRING;
    }

    @Override
    public IConfigBase<List<String>> newInstance() {
        return new ConfigListString(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }
}
