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
public class EntryIntegerEntry extends EntryBaseEntry<Integer> {
    public EntryIntegerEntry(String nameKey, Map.Entry<String, Integer> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public IConfigEntry<Integer> newValueInstance() {
        return new IntegerEntry(this.nameKey, this.value.getValue());
    }

    @Override
    public ConfigType<Map.Entry<String, Integer>> getType() {
        return ConfigTypes.ENTRY_INTEGER;
    }

    @Override
    public IConfigEntry<Map.Entry<String, Integer>> newInstance() {
        return new EntryIntegerEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }

    @Override
    public Codec<Map.Entry<String, Integer>> getCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("key").forGetter(Map.Entry::getKey),
                Codec.INT.fieldOf("value").forGetter(Map.Entry::getValue)
        ).apply(i, AbstractMap.SimpleEntry::new));
    }
}
