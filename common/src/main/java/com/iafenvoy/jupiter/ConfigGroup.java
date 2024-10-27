package com.iafenvoy.jupiter;

import com.iafenvoy.jupiter.interfaces.IConfigBase;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGroup {
    private final List<IConfigBase<?>> configs;
    private final Map<String, IConfigBase<?>> byJsonKey = new HashMap<>();

    public ConfigGroup() {
        this(List.of());
    }

    public ConfigGroup(List<IConfigBase<?>> configs) {
        this.configs = configs;
        for (IConfigBase<?> i : this.configs)
            this.byJsonKey.put(i.getJsonKey(), i);
    }

    public ConfigGroup add(IConfigBase<?> config) {
        this.configs.add(config);
        this.byJsonKey.put(config.getJsonKey(), config);
        return this;
    }

    public List<IConfigBase<?>> getConfigs() {
        return this.configs;
    }

    public ConfigGroup copy() {
        return new ConfigGroup((List<IConfigBase<?>>) (Object) this.configs.stream().map(x -> x.newInstance()).toList());
    }

    public MapCodec<IConfigBase<?>> getCodec() {
        return Codec.STRING.dispatchMap(IConfigBase::getJsonKey, x -> this.byJsonKey.get(x).getConfigCodec());
    }
}
