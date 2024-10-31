package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class MapIntegerEntry extends MapBaseEntry<Integer> {
    public MapIntegerEntry(String nameKey, Map<String, Integer> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<Integer> getValueCodec() {
        return Codec.INT;
    }

    @Override
    public IConfigEntry<Map.Entry<String, Integer>> newSingleInstance(Integer value, String key, Runnable reload) {
        return new EntryIntegerEntry(this.nameKey, new AbstractMap.SimpleEntry<>(key, value)) {
            @Override
            public void reset() {
                MapIntegerEntry.this.getValue().remove(key);
                reload.run();
            }

            @Override
            public void setValue(Map.Entry<String, Integer> value) {
                if (!Objects.equals(this.value.getKey(), value.getKey())) {
                    MapIntegerEntry.this.getValue().remove(this.value.getKey());
                    MapIntegerEntry.this.getValue().put(value.getKey(), value.getValue());
                } else if (!Objects.equals(this.value.getValue(), value.getValue()))
                    MapIntegerEntry.this.getValue().put(this.value.getKey(), value.getValue());
                super.setValue(value);
            }
        };
    }

    @Override
    public Integer newValue() {
        return 0;
    }

    @Override
    public ConfigType<Map<String, Integer>> getType() {
        return ConfigTypes.MAP_INTEGER;
    }

    @Override
    public IConfigEntry<Map<String, Integer>> newInstance() {
        return new MapIntegerEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }
}
