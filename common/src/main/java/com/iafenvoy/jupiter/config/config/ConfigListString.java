package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

import java.util.List;

public class ConfigListString extends ConfigBase<List<String>> {
    private final List<String> defaultValue;
    private List<String> value;

    public ConfigListString(String nameKey, List<String> defaultValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
    }

    @Override
    public ConfigType<List<String>> getType() {
        return ConfigType.LIST_STRING;
    }

    @Override
    public IConfigBase<List<String>> newInstance() {
        return new ConfigListString(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public List<String> getValue() {
        return this.value;
    }

    @Override
    public List<String> getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(List<String> value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<List<String>> getCodec() {
        return Codec.STRING.listOf();
    }
}
