package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

import java.util.Map;

public class ConfigMapString extends ConfigBase<Map<String, String>> {
    private final Map<String, String> defaultValue;
    private Map<String, String> value;

    public ConfigMapString(String nameKey, Map<String, String> defaultValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
    }

    @Override
    public ConfigType<Map<String, String>> getType() {
        return ConfigType.MAP_STRING;
    }

    @Override
    public IConfigBase<Map<String, String>> newInstance() {
        return new ConfigMapString(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Map<String, String> getValue() {
        return this.value;
    }

    @Override
    public Map<String, String> getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(Map<String, String> value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<Map<String, String>> getCodec() {
        return Codec.unboundedMap(Codec.STRING, Codec.STRING);
    }
}
