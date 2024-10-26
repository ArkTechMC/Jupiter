package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.container.AbstractConfigContainer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ServerConfigManager implements SynchronousResourceReloader {
    private static final Map<Identifier, ServerConfigHolder> CONFIGS = new HashMap<>();

    public static void registerServerConfig(AbstractConfigContainer data, PermissionChecker checker) {
        CONFIGS.put(data.getConfigId(), new ServerConfigHolder(data, checker));
    }

    @Nullable
    public static AbstractConfigContainer getConfig(Identifier id) {
        ServerConfigHolder holder = CONFIGS.get(id);
        if (holder == null) return null;
        return holder.data;
    }

    public static boolean checkPermission(Identifier id, MinecraftServer server, ServerPlayerEntity player) {
        ServerConfigHolder holder = CONFIGS.get(id);
        if (holder == null) return false;
        return holder.checker.check(server, player);
    }

    @Override
    public void reload(ResourceManager manager) {
        CONFIGS.values().forEach(x -> x.data.load());
        Jupiter.LOGGER.info("Successfully reload {} server config(s).", CONFIGS.size());
    }

    @FunctionalInterface
    public interface PermissionChecker {
        PermissionChecker ALWAYS_TRUE = (server, player) -> true;
        PermissionChecker ALWAYS_FALSE = (server, player) -> false;
        PermissionChecker IS_DEDICATE_SERVER = (server, player) -> server.isDedicated();
        PermissionChecker IS_LOCAL_GAME = (server, player) -> !IS_DEDICATE_SERVER.check(server, player);
        PermissionChecker IS_OPERATOR = (server, player) -> IS_LOCAL_GAME.check(server, player) || player.hasPermissionLevel(server.getOpPermissionLevel());

        boolean check(MinecraftServer server, ServerPlayerEntity player);
    }

    private record ServerConfigHolder(AbstractConfigContainer data, PermissionChecker checker) {
    }
}
