package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.mojang.serialization.Codec;

import java.util.List;

public class ListDoubleEntry extends ListBaseEntry<Double> {
    public ListDoubleEntry(String nameKey, List<Double> defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public Codec<Double> getValueCodec() {
        return Codec.INT;
    }

    @Override
    public IConfigEntry<Double> newSingleInstance(Double value, int index, Runnable reload) {
        return new DoubleEntry(this.nameKey, value) {
            @Override
            public void reset() {
                ListDoubleEntry.this.getValue().remove(index);
                reload.run();
            }

            @Override
            public void setValue(Double value) {
                super.setValue(value);
                ListDoubleEntry.this.getValue().set(index, value);
            }
        };
    }

    @Override
    public Double newValue() {
        return (double) 0;
    }

    @Override
    public ConfigType<List<Double>> getType() {
        return ConfigTypes.LIST_DOUBLE;
    }

    @Override
    public IConfigEntry<List<Double>> newInstance() {
        return new ListDoubleEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }
}
