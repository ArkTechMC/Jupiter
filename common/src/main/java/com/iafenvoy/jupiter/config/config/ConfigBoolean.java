package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.mojang.serialization.Codec;

public class ConfigBoolean extends ConfigBase<Boolean> {
    private final boolean defaultValue;
    private boolean value;

    public ConfigBoolean(String nameKey, boolean defaultValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
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
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }
}
