package com.iafenvoy.jupiter.util;

import com.iafenvoy.jupiter.Jupiter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public class ReflectUtil {
    @Nullable
    public static Class<?> getClassUnsafely(String name) {
        try {
            return Class.forName(name);
        } catch (ReflectiveOperationException e) {
            Jupiter.LOGGER.error("Failed to get class", e);
            return null;
        }
    }

    @Nullable
    public static <V> V constructUnsafely(Class<V> cls) {
        try {
            Constructor<V> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            Jupiter.LOGGER.error("Failed to construct object");
            return null;
        }
    }
}
