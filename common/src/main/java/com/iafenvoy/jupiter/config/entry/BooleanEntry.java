package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

public class BooleanEntry extends BaseEntry<Boolean> {
    public BooleanEntry(String nameKey, boolean defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<Boolean> getType() {
        return ConfigTypes.BOOLEAN;
    }

    @Override
    public IConfigEntry<Boolean> newInstance() {
        return new BooleanEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }
}
