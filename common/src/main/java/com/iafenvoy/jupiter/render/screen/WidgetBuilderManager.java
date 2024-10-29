package com.iafenvoy.jupiter.render.screen;

import com.iafenvoy.jupiter.config.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.render.widget.ButtonWidgetBuilder;
import com.iafenvoy.jupiter.render.widget.TextFieldWidgetBuilder;
import com.iafenvoy.jupiter.render.widget.WidgetBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class WidgetBuilderManager {
    private static final Map<ConfigType<?>, Function<IConfigEntry<?>, WidgetBuilder<?, ?>>> BUILDERS = new HashMap<>();

    public static <T> void register(ConfigType<T> type, Function<IConfigEntry<T>, WidgetBuilder<T, ?>> builder) {
        BUILDERS.put(type, (Function<IConfigEntry<?>, WidgetBuilder<?, ?>>) (Object) builder);
    }

    public static <T> WidgetBuilder<T, ?> get(IConfigEntry<T> entry) {
        return (WidgetBuilder<T, ?>) BUILDERS.get(entry.getType()).apply(entry);
    }

    static {
        register(ConfigType.BOOLEAN, config -> new ButtonWidgetBuilder<>(config, button -> config.setValue(!config.getValue()), () -> Text.of(config.getValue() ? "§atrue" : "§cfalse")));
        register(ConfigType.INTEGER, TextFieldWidgetBuilder::new);
        register(ConfigType.DOUBLE, TextFieldWidgetBuilder::new);
    }
}
