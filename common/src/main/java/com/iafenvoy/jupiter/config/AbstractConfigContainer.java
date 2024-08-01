package com.iafenvoy.jupiter.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import com.iafenvoy.jupiter.malilib.config.ConfigUtils;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConfigContainer implements IConfigHandler {
    protected final List<ConfigCategory> configTabs = new ArrayList<>();
    protected final Identifier id;
    protected final String titleNameKey;

    public AbstractConfigContainer(Identifier id, String titleNameKey) {
        this.id = id;
        this.titleNameKey = titleNameKey;
    }

    public Identifier getConfigId() {
        return this.id;
    }

    public String getTitleNameKey() {
        return this.titleNameKey;
    }

    public ConfigCategory createTab(String id, String translateKey) {
        ConfigCategory category = new ConfigCategory(id, translateKey, new ArrayList<>());
        this.configTabs.add(category);
        return category;
    }

    public List<ConfigCategory> getConfigTabs() {
        return this.configTabs;
    }

    public String serialize() {
        JsonObject configRoot = new JsonObject();
        for (ConfigCategory category : this.getConfigTabs())
            ConfigUtils.writeConfigBase(configRoot, category.id(), category.getConfigs(), this.shouldCompressKey());
        this.writeCustomData(configRoot);
        return configRoot.toString();
    }

    public void deserialize(String data) {
        JsonElement jsonElement = JsonParser.parseString(data);
        if (jsonElement instanceof JsonObject obj) {
            if (!this.shouldLoad(obj)) return;
            for (ConfigCategory category : this.getConfigTabs())
                ConfigUtils.readConfigBase(obj, category.id(), category.getConfigs(), this.shouldCompressKey());
            this.readCustomData(obj);
        }
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

    public record ConfigCategory(String id, String translateKey, List<IConfigBase> configs) {
        public <T extends IConfigBase> ConfigCategory addConfig(T config) {
            this.configs.add(config);
            return this;
        }

        public <T extends IConfigBase> T add(T config) {
            this.configs.add(config);
            return config;
        }

        public List<IConfigBase> getConfigs() {
            return ImmutableList.copyOf(this.configs);
        }

        public ConfigCategory copy() {
            return new ConfigCategory(this.id, this.translateKey, this.configs.stream().map(IConfigBase::copy).toList());
        }
    }
}
