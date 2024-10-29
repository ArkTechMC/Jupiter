package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

public class BooleanEntry extends BaseEntry<Boolean> {
    public BooleanEntry(String nameKey, boolean defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<Boolean> getType() {
        return ConfigType.BOOLEAN;
    }

    @Override
    public IConfigEntry<Boolean> newInstance() {
        return new BooleanEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }
}
