package com.iafenvoy.jupiter.api;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.iafenvoy.jupiter.Jupiter;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigData implements IConfigHandler {
    protected static final Gson GSON = new GsonBuilder().create();
    private final List<ConfigCategory> configTabs = new ArrayList<>();
    private final String modId;
    private final String titleNameKey;
    private final String path;

    public ConfigData(String modId, String titleNameKey, String path) {
        this.modId = modId;
        this.titleNameKey = titleNameKey;
        this.path = path;
        this.init();
        ConfigManager.getInstance().registerConfigHandler(modId, this);
    }

    public abstract void init();

    public String getModId() {
        return this.modId;
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
            ConfigUtils.writeConfigBase(configRoot, category.id(), category.getConfigs());
        return configRoot.toString();
    }

    public void deserialize(String data) {
        JsonElement jsonElement = JsonParser.parseString(data);
        if (jsonElement instanceof JsonObject obj)
            for (ConfigCategory category : this.getConfigTabs())
                ConfigUtils.readConfigBase(obj, category.id(), category.getConfigs());
    }

    @Override
    public void load() {
        try {
            this.deserialize(FileUtils.readFileToString(new File(this.path), StandardCharsets.UTF_8));
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to load config: {}", this.path, e);
        }
    }

    @Override
    public void save() {
        try {
            FileUtils.write(new File(this.path), this.serialize(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to load config: {}", this.path, e);
        }
    }

    public record ConfigCategory(String id, String translateKey, List<IConfigBase> configs) {
        public <T extends IConfigBase> ConfigCategory addConfig(T config) {
            this.configs.add(config);
            return this;
        }

        public List<IConfigBase> getConfigs() {
            return ImmutableList.copyOf(this.configs);
        }
    }
}
