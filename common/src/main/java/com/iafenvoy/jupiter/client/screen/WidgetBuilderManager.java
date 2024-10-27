package com.iafenvoy.jupiter.client.screen;

import com.iafenvoy.jupiter.ConfigType;
import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.iafenvoy.jupiter.client.widget.ButtonWidgetBuilder;
import com.iafenvoy.jupiter.client.widget.IWidgetBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WidgetBuilderManager {
    private static final Map<ConfigType<?>, IWidgetBuilder<?>> BUILDERS = new HashMap<>();

    public static <T> void register(ConfigType<T> type, IWidgetBuilder<T> builder) {
        BUILDERS.put(type, builder);
    }

    public static <T> ClickableWidget create(int x, int y, int width, int height, IConfigBase<T> parent) {
        ConfigType<T> type = parent.getType();
        if (BUILDERS.containsKey(type))
            return ((IWidgetBuilder<T>) BUILDERS.get(type)).build(x, y, width, height, parent);
        throw new IllegalArgumentException("Unknown config type from " + parent.getNameKey());
    }

    static {
        register(ConfigType.BOOLEAN, new ButtonWidgetBuilder<>(config -> button -> config.setValue(!config.getValue())));
    }
}
