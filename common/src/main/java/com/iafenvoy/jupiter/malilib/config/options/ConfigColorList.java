package com.iafenvoy.jupiter.malilib.config.options;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.iafenvoy.jupiter.malilib.MaLiLib;
import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigColorList;
import com.iafenvoy.jupiter.malilib.util.Color4f;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public class ConfigColorList extends ConfigBase<ConfigColorList> implements IConfigColorList {
    private final ImmutableList<Color4f> defaultValue;
    private final List<Color4f> colors = new ArrayList<>();

    public ConfigColorList(String nameKey, ImmutableList<Color4f> defaultValue) {
        this(nameKey, defaultValue, nameKey + COMMENT_SUFFIX);
    }

    public ConfigColorList(String nameKey, ImmutableList<Color4f> defaultValue, String comment) {
        super(ConfigType.COLOR_LIST, nameKey, comment);
        this.defaultValue = defaultValue;
        this.colors.addAll(defaultValue);
    }

    @Override
    public List<Color4f> getColors() {
        return this.colors;
    }

    @Override
    public void setColors(List<Color4f> colors) {
        if (!this.colors.equals(colors)) {
            this.colors.clear();
            this.colors.addAll(colors);
            this.onValueChanged();
        }
    }

    @Override
    public ImmutableList<Color4f> getDefaultColors() {
        return this.defaultValue;
    }

    @Override
    public void resetToDefault() {
        this.setColors(this.defaultValue);
    }

    @Override
    public boolean isModified() {
        return !this.colors.equals(this.defaultValue);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        this.colors.clear();
        try {
            if (element.isJsonArray()) {
                JsonArray arr = element.getAsJsonArray();
                final int count = arr.size();
                for (int i = 0; i < count; ++i) {
                    this.colors.add(Color4f.fromColor(StringUtils.getColor(arr.get(i).getAsString(), 0)));
                }
            } else {
                MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element);
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getNameKey(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonArray arr = new JsonArray();

        for (Color4f color4f : this.colors) {
            arr.add(new JsonPrimitive(color4f.toString()));
        }

        return arr;
    }

    @Override
    public IConfigBase copy() {
        return new ConfigColorList(this.getNameKey(), this.defaultValue, this.getCommentKey());
    }

    @Override
    public Codec<?> getCodec() {
        return Color4f.CODEC.listOf().orElse(this.defaultValue);
    }
}
