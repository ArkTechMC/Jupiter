package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import com.iafenvoy.jupiter.config.interfaces.IConfigEnumEntry;
import com.mojang.serialization.Codec;

public class ConfigEnum extends ConfigBase<IConfigEnumEntry> {
    private final IConfigEnumEntry defaultValue;
    private IConfigEnumEntry value;

    public ConfigEnum(String nameKey, IConfigEnumEntry defaultValue) {
        super(nameKey);
        this.defaultValue = this.value = defaultValue;
    }

    @Override
    public ConfigType<IConfigEnumEntry> getType() {
        return ConfigType.ENUM;
    }

    @Override
    public IConfigBase<IConfigEnumEntry> newInstance() {
        return new ConfigEnum(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public IConfigEnumEntry getValue() {
        return this.value;
    }

    @Override
    public IConfigEnumEntry getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setValue(IConfigEnumEntry value) {
        this.value = value;
        super.setValue(value);
    }

    @Override
    public Codec<IConfigEnumEntry> getCodec() {
        return Codec.STRING.xmap(this.defaultValue::getByName, IConfigEnumEntry::getName);
    }
}
