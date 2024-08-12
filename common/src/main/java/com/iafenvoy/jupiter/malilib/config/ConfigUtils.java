package com.iafenvoy.jupiter.malilib.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.iafenvoy.jupiter.malilib.config.options.ConfigTypeWrapper;
import com.iafenvoy.jupiter.malilib.util.JsonUtils;

import java.util.List;

public class ConfigUtils {
    private static String compressKey(String name) {
        String[] s = name.split("\\.");
        if (s.length == 0) return "";
        return s[s.length - 1];
    }

    public static void readConfigBase(JsonObject root, String category, List<? extends IConfigBase> options, boolean compress) {
        JsonObject obj = JsonUtils.getNestedObject(root, category, false);

        if (obj != null) {
            for (IConfigBase option : options) {
                String name = option.getNameKey();
                if (compress) name = compressKey(name);
                if (obj.has(name))
                    option.setValueFromJsonElement(obj.get(name));
            }
        }
    }

    public static void writeConfigBase(JsonObject root, String category, List<? extends IConfigBase> options, boolean compress, boolean shouldSaveFully) {
        JsonObject obj = JsonUtils.getNestedObject(root, category, true);
        assert obj != null;

        for (IConfigBase option : options) {
            if (shouldSaveFully || !(option instanceof IConfigResettable resettable) || resettable.isModified()) {
                String name = option.getNameKey();
                if (compress) name = compressKey(name);
                obj.add(name, option.getAsJsonElement());
            }
        }
    }

    /**
     * Creates a wrapper for the provided configs that pretends to be another type.<br>
     * This is useful for example for enum configs, which may contain two values per entry.<br>
     * <b>** WARNING **</b>: The configs in toWrap are assumed to actually implement the
     * interface that the wrapped type is of!! Otherwise things will crash!
     *
     */
    public static List<ConfigTypeWrapper> createConfigWrapperForType(ConfigType wrappedType, List<? extends IConfigValue> toWrap) {
        ImmutableList.Builder<ConfigTypeWrapper> builder = ImmutableList.builder();

        for (IConfigValue iConfigValue : toWrap) {
            builder.add(new ConfigTypeWrapper(wrappedType, iConfigValue));
        }

        return builder.build();
    }
}
