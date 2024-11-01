package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.config.entry.EntryBaseEntry;
import com.iafenvoy.jupiter.config.entry.ListBaseEntry;
import com.iafenvoy.jupiter.config.entry.MapBaseEntry;
import com.iafenvoy.jupiter.config.type.ConfigType;
import com.iafenvoy.jupiter.config.type.ConfigTypes;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import com.iafenvoy.jupiter.render.widget.builder.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class WidgetBuilderManager {
    private static final Map<ConfigType<?>, Function<IConfigEntry<?>, WidgetBuilder<?>>> BUILDERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void register(ConfigType<T> type, Function<IConfigEntry<T>, WidgetBuilder<T>> builder) {
        BUILDERS.put(type, (Function<IConfigEntry<?>, WidgetBuilder<?>>) (Object) builder);
    }

    @SuppressWarnings("unchecked")
    public static <T> WidgetBuilder<T> get(IConfigEntry<T> entry) {
        return (WidgetBuilder<T>) BUILDERS.get(entry.getType()).apply(entry);
    }

    static {
        register(ConfigTypes.SEPARATOR,SeparatorWidgetBuilder::new);
        register(ConfigTypes.BOOLEAN, config -> new ButtonWidgetBuilder<>(config, button -> config.setValue(!config.getValue()), () -> Text.of(config.getValue() ? "§atrue" : "§cfalse")));
        register(ConfigTypes.INTEGER, TextFieldWidgetBuilder::new);
        register(ConfigTypes.DOUBLE, TextFieldWidgetBuilder::new);
        register(ConfigTypes.STRING, TextFieldWidgetBuilder::new);
        register(ConfigTypes.ENUM, config -> new ButtonWidgetBuilder<>(config, button -> config.setValue(config.getValue().cycle(true)), () -> Text.translatable(config.getValue().getName())));
        register(ConfigTypes.LIST_STRING, config -> new ListWidgetBuilder<>((ListBaseEntry<String>) config));
        register(ConfigTypes.LIST_INTEGER, config -> new ListWidgetBuilder<>((ListBaseEntry<Integer>) config));
        register(ConfigTypes.LIST_DOUBLE, config -> new ListWidgetBuilder<>((ListBaseEntry<Double>) config));
        register(ConfigTypes.MAP_STRING, config -> new MapWidgetBuilder<>((MapBaseEntry<String>) config));
        register(ConfigTypes.MAP_INTEGER, config -> new MapWidgetBuilder<>((MapBaseEntry<Integer>) config));
        register(ConfigTypes.MAP_DOUBLE, config -> new MapWidgetBuilder<>((MapBaseEntry<Double>) config));
        register(ConfigTypes.ENTRY_STRING, config -> new EntryWidgetBuilder<>((EntryBaseEntry<String>) config));
        register(ConfigTypes.ENTRY_INTEGER, config -> new EntryWidgetBuilder<>((EntryBaseEntry<Integer>) config));
        register(ConfigTypes.ENTRY_DOUBLE, config -> new EntryWidgetBuilder<>((EntryBaseEntry<Double>) config));
        register(ConfigTypes.IDENTIFIER, TextFieldWidgetBuilder::new);
    }
}
