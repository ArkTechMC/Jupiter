package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class MapDoubleEntry extends MapBaseEntry<Double> {
    public MapDoubleEntry(String nameKey, Map<String, Double> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<Double> getValueCodec() {
        return Codec.DOUBLE;
    }

    @Override
    public IConfigEntry<Map.Entry<String, Double>> newSingleInstance(Double value, String key, Runnable reload) {
        return new EntryDoubleEntry(this.nameKey, new AbstractMap.SimpleEntry<>(key, value)) {
            @Override
            public void reset() {
                MapDoubleEntry.this.getValue().remove(key);
                reload.run();
            }

            @Override
            public void setValue(Map.Entry<String, Double> value) {
                if (!Objects.equals(this.value.getKey(), value.getKey())) {
                    MapDoubleEntry.this.getValue().remove(this.value.getKey());
                    MapDoubleEntry.this.getValue().put(value.getKey(), value.getValue());
                } else if (!Objects.equals(this.value.getValue(), value.getValue()))
                    MapDoubleEntry.this.getValue().put(this.value.getKey(), value.getValue());
                super.setValue(value);
            }
        };
    }

    @Override
    public Double newValue() {
        return (double) 0;
    }

    @Override
    public ConfigType<Map<String, Double>> getType() {
        return ConfigTypes.MAP_DOUBLE;
    }

    @Override
    public IConfigEntry<Map<String, Double>> newInstance() {
        return new MapDoubleEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }
}
