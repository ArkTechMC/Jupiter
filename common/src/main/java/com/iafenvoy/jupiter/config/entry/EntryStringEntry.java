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
public class EntryStringEntry extends EntryBaseEntry<String> {
    public EntryStringEntry(String nameKey, Map.Entry<String, String> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public IConfigEntry<String> newValueInstance() {
        return new StringEntry(this.nameKey, this.value.getValue());
    }

    @Override
    public ConfigType<Map.Entry<String, String>> getType() {
        return ConfigTypes.ENTRY_STRING;
    }

    @Override
    public IConfigEntry<Map.Entry<String, String>> newInstance() {
        return new EntryStringEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }

    @Override
    public Codec<Map.Entry<String, String>> getCodec() {
        return RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("key").forGetter(Map.Entry::getKey),
                Codec.STRING.fieldOf("value").forGetter(Map.Entry::getValue)
        ).apply(i, AbstractMap.SimpleEntry::new));
    }
}
