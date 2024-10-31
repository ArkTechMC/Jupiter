package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.AbstractMap;
import java.util.Map;

@ApiStatus.Internal
public class EntryDoubleEntry extends EntryBaseEntry<Double> {
    public EntryDoubleEntry(String nameKey, Map.Entry<String, Double> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public IConfigEntry<Double> newValueInstance() {
        return new DoubleEntry(this.nameKey, this.value.getValue());
    }

    @Override
    public ConfigType<Map.Entry<String, Double>> getType() {
        return ConfigTypes.ENTRY_DOUBLE;
    }

    @Override
    public IConfigEntry<Map.Entry<String, Double>> newInstance() {
        return new EntryDoubleEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }

    @Override
    public Codec<Map.Entry<String, Double>> getCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("key").forGetter(Map.Entry::getKey),
                Codec.DOUBLE.fieldOf("value").forGetter(Map.Entry::getValue)
        ).apply(i, AbstractMap.SimpleEntry::new));
    }
}
