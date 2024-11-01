package com.iafenvoy.jupiter.config.type;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.mojang.datafixers.util.Unit;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

public interface ConfigTypes {
    ConfigType<Unit> SEPARATOR = new SingleConfigType<>();
    ConfigType<Boolean> BOOLEAN = new SingleConfigType<>();
    ConfigType<Integer> INTEGER = new SingleConfigType<>();
    ConfigType<Double> DOUBLE = new SingleConfigType<>();
    ConfigType<String> STRING = new SingleConfigType<>();
    @ApiStatus.Internal
    ConfigType<Map.Entry<String, String>> ENTRY_STRING = new SingleConfigType<>();
    @ApiStatus.Internal
    ConfigType<Map.Entry<String, Integer>> ENTRY_INTEGER = new SingleConfigType<>();
    @ApiStatus.Internal
    ConfigType<Map.Entry<String, Double>> ENTRY_DOUBLE = new SingleConfigType<>();
    ConfigType<IConfigEnumEntry> ENUM = new SingleConfigType<>();
    ConfigType<List<String>> LIST_STRING = new ListConfigType<>(STRING);
    ConfigType<List<Integer>> LIST_INTEGER = new ListConfigType<>(INTEGER);
    ConfigType<List<Double>> LIST_DOUBLE = new ListConfigType<>(DOUBLE);
    ConfigType<Map<String, String>> MAP_STRING = new MapConfigType<>(STRING);
    ConfigType<Map<String, Integer>> MAP_INTEGER = new MapConfigType<>(INTEGER);
    ConfigType<Map<String, Double>> MAP_DOUBLE = new MapConfigType<>(DOUBLE);
    ConfigType<Identifier> IDENTIFIER = new SingleConfigType<>();
}
