package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.config.interfaces.IConfigEnumEntry;

import java.util.List;
import java.util.Map;

public class ConfigType<T> {
    public static final ConfigType<Boolean> BOOLEAN = new ConfigType<>();
    public static final ConfigType<Integer> INTEGER = new ConfigType<>();
    public static final ConfigType<Double> DOUBLE = new ConfigType<>();
    public static final ConfigType<String> STRING = new ConfigType<>();
    public static final ConfigType<IConfigEnumEntry> ENUM = new ConfigType<>();
    public static final ConfigType<List<String>> LIST_STRING = new ConfigType<>();
    public static final ConfigType<Map<String, String>> MAP_STRING = new ConfigType<>();
}
