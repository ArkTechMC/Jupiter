package com.iafenvoy.jupiter.malilib.config;

public interface IConfigOptionList {
    IConfigOptionListEntry getOptionListValue();

    void setOptionListValue(IConfigOptionListEntry value);

    IConfigOptionListEntry getDefaultOptionListValue();
}
