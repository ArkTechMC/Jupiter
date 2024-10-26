package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

public class ConfigString extends ConfigBase<String> {
    private final String defaultValue;
    private String value;

    public ConfigString(String nameKey, String defaultValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
    }

    @Override
    public ConfigType<String> getType() {
        return ConfigType.STRING;
    }

    @Override
    public IConfigBase<String> newInstance() {
        return new ConfigString(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<String> getCodec() {
        return Codec.STRING;
    }
}
