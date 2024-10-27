package com.iafenvoy.jupiter.config.holder;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.iafenvoy.jupiter.interfaces.ITextFieldConfig;
import com.mojang.serialization.Codec;

public class ConfigString extends ConfigBase<String> implements ITextFieldConfig {
    public ConfigString(String nameKey, String defaultValue) {
        super(nameKey, defaultValue);
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
    public Codec<String> getCodec() {
        return Codec.STRING;
    }

    @Override
    public String valueAsString() {
        return this.getValue();
    }

    @Override
    public void setValueFromString(String s) {
        this.setValue(s);
    }
}
