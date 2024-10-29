package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.Map;

public class MapStringEntry extends MapBaseEntry<String> {
    public MapStringEntry(String nameKey, Map<String, String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<String> getValueCodec() {
        return Codec.STRING;
    }

    @Override
    public ConfigType<Map<String, String>> getType() {
        return ConfigType.MAP_STRING;
    }

    @Override
    public IConfigEntry<Map<String, String>> newInstance() {
        return new MapStringEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }
}
