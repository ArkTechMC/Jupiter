package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.mojang.serialization.Codec;

public class EnumEntry extends BaseEntry<IConfigEnumEntry> {
    public EnumEntry(String nameKey, IConfigEnumEntry defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<IConfigEnumEntry> getType() {
        return ConfigTypes.ENUM;
    }

    @Override
    public IConfigEntry<IConfigEnumEntry> newInstance() {
        return new EnumEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Codec<IConfigEnumEntry> getCodec() {
        return Codec.STRING.xmap(this.defaultValue::getByName, IConfigEnumEntry::getName);
    }
}
