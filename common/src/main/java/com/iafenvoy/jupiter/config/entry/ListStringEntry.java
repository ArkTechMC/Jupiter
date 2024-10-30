package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
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
    public IConfigEntry<String> newSingleInstance(String value, int index, Runnable reload) {
        return new StringEntry(this.nameKey, value) {
            @Override
            public void reset() {
                ListStringEntry.this.getValue().remove(index);
                reload.run();
            }

            @Override
            public void setValue(String value) {
                super.setValue(value);
                ListStringEntry.this.getValue().set(index, value);
            }
        };
    }

    @Override
    public String newValue() {
        return "";
    }

    @Override
    public ConfigType<List<String>> getType() {
        return ConfigTypes.LIST_STRING;
    }

    @Override
    public IConfigEntry<List<String>> newInstance() {
        return new ListStringEntry(this.nameKey, this.defaultValue).comment(this.commentKey).json(this.jsonKey);
    }
}
