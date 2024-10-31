package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class MapStringEntry extends MapBaseEntry<String> {
    public MapStringEntry(String nameKey, Map<String, String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<String> getValueCodec() {
        return Codec.STRING;
    }

    @Override
    public IConfigEntry<Map.Entry<String, String>> newSingleInstance(String value, String key, Runnable reload) {
        return new EntryStringEntry(this.nameKey, new AbstractMap.SimpleEntry<>(key, value)) {
            @Override
            public void reset() {
                MapStringEntry.this.getValue().remove(key);
                reload.run();
            }

            @Override
            public void setValue(Map.Entry<String, String> value) {
                if (!Objects.equals(this.value.getKey(), value.getKey())) {
                    MapStringEntry.this.getValue().remove(this.value.getKey());
                    MapStringEntry.this.getValue().put(value.getKey(), value.getValue());
                } else if (!Objects.equals(this.value.getValue(), value.getValue()))
                    MapStringEntry.this.getValue().put(this.value.getKey(), value.getValue());
                super.setValue(value);
            }
        };
    }

    @Override
    public String newValue() {
        return "";
    }

    @Override
    public ConfigType<Map<String, String>> getType() {
        return ConfigTypes.MAP_STRING;
    }

    @Override
    public IConfigEntry<Map<String, String>> newInstance() {
        return new MapStringEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }
}
