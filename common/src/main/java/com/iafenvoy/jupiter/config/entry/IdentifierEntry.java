package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.ITextFieldConfig;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class IdentifierEntry extends BaseEntry<Identifier> implements ITextFieldConfig {
    public IdentifierEntry(String nameKey, Identifier defaultValue) {
        super(nameKey, defaultValue);
    }

    @Override
    public ConfigType<Identifier> getType() {
        return ConfigTypes.IDENTIFIER;
    }

    @Override
    public IConfigEntry<Identifier> newInstance() {
        return new IdentifierEntry(this.nameKey, this.defaultValue).visible(this.visible).json(this.jsonKey);
    }

    @Override
    public Codec<Identifier> getCodec() {
        return Identifier.CODEC;
    }

    @Override
    public String valueAsString() {
        return this.getValue().toString();
    }

    @Override
    public void setValueFromString(String s) {
        this.setValue(Objects.requireNonNull(Identifier.tryParse(s)));
    }
}
