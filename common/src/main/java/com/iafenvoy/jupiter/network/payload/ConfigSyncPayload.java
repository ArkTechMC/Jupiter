package com.iafenvoy.jupiter.network.payload;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncPayload(Identifier id, boolean allow, NbtCompound compound) implements CustomPayload {
    public static final Id<ConfigSyncPayload> ID = new Id<>(Identifier.of(Jupiter.MOD_ID, "config_sync"));
    public static final PacketCodec<PacketByteBuf, ConfigSyncPayload> CODEC = PacketCodec.of((value, buf) -> {
        buf.writeIdentifier(value.id);
        buf.writeBoolean(value.allow);
        buf.writeNbt(value.compound);
    }, buf -> new ConfigSyncPayload(buf.readIdentifier(), buf.readBoolean(), buf.readNbt()));

    public ConfigSyncPayload(Identifier id, NbtCompound compound) {
        this(id, true, compound);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
