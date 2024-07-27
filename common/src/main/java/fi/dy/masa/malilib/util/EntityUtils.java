package fi.dy.masa.malilib.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {
    /**
     * Returns the camera entity, if it's not null, otherwise returns the client player entity.
     *
     * @return
     */
    @Nullable
    public static Entity getCameraEntity() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Entity entity = mc.getCameraEntity();

        if (entity == null) {
            entity = mc.player;
        }

        return entity;
    }
}
