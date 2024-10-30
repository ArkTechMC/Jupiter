package com.iafenvoy.jupiter.config.type;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

public interface ConfigTypes {
    ConfigType<Boolean> BOOLEAN = new SingleConfigType<>();
    ConfigType<Integer> INTEGER = new SingleConfigType<>();
    ConfigType<Double> DOUBLE = new SingleConfigType<>();
    ConfigType<String> STRING = new SingleConfigType<>();
    @ApiStatus.Internal
    ConfigType<Map.Entry<String,String>> ENTRY_STRING = new SingleConfigType<>();
    ConfigType<IConfigEnumEntry> ENUM = new SingleConfigType<>();
    ConfigType<List<String>> LIST_STRING = new ListConfigType<>(STRING);
    ConfigType<Map<String, String>> MAP_STRING = new MapConfigType<>(STRING);
}
