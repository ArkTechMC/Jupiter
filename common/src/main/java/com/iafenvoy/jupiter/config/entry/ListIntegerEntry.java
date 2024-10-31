package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.List;

public class ListIntegerEntry extends ListBaseEntry<Integer> {
    public ListIntegerEntry(String nameKey, List<Integer> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<Integer> getValueCodec() {
        return Codec.INT;
    }

    @Override
    public IConfigEntry<Integer> newSingleInstance(Integer value, int index, Runnable reload) {
        return new IntegerEntry(this.nameKey, value) {
            @Override
            public void reset() {
                ListIntegerEntry.this.getValue().remove(index);
                reload.run();
            }

            @Override
            public void setValue(Integer value) {
                super.setValue(value);
                ListIntegerEntry.this.getValue().set(index, value);
            }
        };
    }

    @Override
    public Integer newValue() {
        return 0;
    }

    @Override
    public ConfigType<List<Integer>> getType() {
        return ConfigTypes.LIST_INTEGER;
    }

    @Override
    public IConfigEntry<List<Integer>> newInstance() {
        return new ListIntegerEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }
}
