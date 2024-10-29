package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.ITextFieldConfig;
import com.mojang.serialization.Codec;

public class StringEntry extends BaseEntry<String> implements ITextFieldConfig {
    public StringEntry(String nameKey, String defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<String> getType() {
        return ConfigType.STRING;
    }

    @Override
    public IConfigEntry<String> newInstance() {
        return new StringEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
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
