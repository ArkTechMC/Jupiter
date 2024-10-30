package com.iafenvoy.jupiter.config.container;

import com.google.gson.*;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.ConfigGroup;
import com.iafenvoy.jupiter.interfaces.IConfig;
import com.iafenvoy.jupiter.malilib.config.IConfigHandler;
import com.mojang.serialization.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractConfigContainer implements IConfigHandler, IConfig {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected final List<ConfigGroup> configTabs = new ArrayList<>();
    protected final Identifier id;
    protected final String titleNameKey;
    private Codec<List<ConfigGroup>> cache;

    public AbstractConfigContainer(Identifier id, String titleNameKey) {
        this.id = id;
        this.titleNameKey = titleNameKey;
    }

    @Override
    public Identifier getConfigId() {
        return this.id;
    }

    @Override
    public String getTitleNameKey() {
        return this.titleNameKey;
    }

    public ConfigGroup createTab(String id, String translateKey) {
        ConfigGroup category = new ConfigGroup(id, translateKey, new ArrayList<>());
        this.configTabs.add(category);
        this.cache = null;
        return category;
    }

    public List<ConfigGroup> getConfigTabs() {
        return this.configTabs;
    }

    public String serialize() {
        if (this.cache == null) this.cache = this.buildCodec();
        return GSON.toJson(this.cache.encodeStart(JsonOps.INSTANCE, this.configTabs).getOrThrow(false, Jupiter.LOGGER::error));
    }

    public void deserialize(String data) {
        JsonElement jsonElement = JsonParser.parseString(data);
        if (jsonElement instanceof JsonObject obj) {
            if (!this.shouldLoad(obj)) return;
            this.deserializeWithoutCheck(data);
        }
    }

    @ApiStatus.Internal
    public final void deserializeWithoutCheck(String data) {
        if (this.cache == null) this.cache = this.buildCodec();
        this.cache.parse(JsonOps.INSTANCE, JsonParser.parseString(data));
    }

    protected Codec<List<ConfigGroup>> buildCodec() {
        return MapCodec.<List<ConfigGroup>>of(new MapEncoder.Implementation<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return AbstractConfigContainer.this.configTabs.stream().map(ConfigGroup::getId).map(ops::createString);
            }

            @Override
            public <T> RecordBuilder<T> encode(List<ConfigGroup> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return input.stream().reduce(prefix, (p, c) -> p.add(c.getId(), c.encode(ops)), (a, b) -> null);
            }
        }, new MapDecoder.Implementation<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return AbstractConfigContainer.this.configTabs.stream().map(ConfigGroup::getId).map(ops::createString);
            }

            @Override
            public <T> DataResult<List<ConfigGroup>> decode(DynamicOps<T> ops, MapLike<T> input) {
                input.entries().forEach(x -> {
                    String s = ops.getStringValue(x.getFirst()).getOrThrow(false, Jupiter.LOGGER::error);
                    AbstractConfigContainer.this.configTabs.stream().filter(y -> y.getId().equals(s)).findFirst().ifPresent(y -> y.decode(ops, x.getSecond()));
                });
                return DataResult.success(AbstractConfigContainer.this.configTabs);
            }
        }).codec();
    }

    //Can be used to check version, etc
    protected boolean shouldLoad(JsonObject obj) {
        return true;
    }

    protected void readCustomData(JsonObject obj) {
    }

    protected void writeCustomData(JsonObject obj) {
    }

    protected boolean shouldCompressKey() {
        return true;
    }

    protected SaveFullOption saveFullOption() {
        return SaveFullOption.LOCAL;
    }

    protected enum SaveFullOption {
        NONE(false, false), LOCAL(true, false), ALL(true, true);

        private final boolean local, network;

        SaveFullOption(boolean local, boolean network) {
            this.local = local;
            this.network = network;
        }

        public boolean shouldSaveFully(boolean isLocal) {
            return isLocal ? this.local : this.network;
        }
    }
}
