package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.mojang.serialization.Codec;

public class ConfigEnum extends ConfigBase<IConfigEnumEntry> {
    public ConfigEnum(String nameKey, IConfigEnumEntry defaultValue) {
        super(nameKey,defaultValue);
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
    public Codec<IConfigEnumEntry> getCodec() {
        return Codec.STRING.xmap(this.defaultValue::getByName, IConfigEnumEntry::getName);
    }
}
