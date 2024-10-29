package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.List;

public class ListStringEntry extends ListBaseEntry<String> {
    public ListStringEntry(String nameKey, List<String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<String> getValueCodec() {
        return Codec.STRING;
    }

    @Override
    public ConfigType<List<String>> getType() {
        return ConfigType.LIST_STRING;
    }

    @Override
    public IConfigEntry<List<String>> newInstance() {
        return new ListStringEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }
}
