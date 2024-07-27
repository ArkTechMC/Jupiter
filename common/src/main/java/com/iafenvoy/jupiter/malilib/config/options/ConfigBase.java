package com.iafenvoy.jupiter.malilib.config.options;

import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigNotifiable;
import com.iafenvoy.jupiter.malilib.interfaces.IValueChangeCallback;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import com.iafenvoy.jupiter.malilib.config.IConfigResettable;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigBase<T extends IConfigBase> implements IConfigBase, IConfigResettable, IConfigNotifiable<T> {
    private final ConfigType type;
    private final String name;
    private final String prettyName;
    private String comment;
    @Nullable
    private IValueChangeCallback<T> callback;

    public ConfigBase(ConfigType type, String name, String comment) {
        this(type, name, comment, name);
    }

    public ConfigBase(ConfigType type, String name, String comment, String prettyName) {
        this.type = type;
        this.name = name;
        this.prettyName = prettyName;
        this.comment = comment;
    }

    @Override
    public ConfigType getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPrettyName() {
        return StringUtils.translate(this.prettyName);
    }

    @Override
    @Nullable
    public String getComment() {
        return StringUtils.getTranslatedOrFallback("config.comment." + this.getName().toLowerCase(), this.comment);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setValueChangeCallback(IValueChangeCallback<T> callback) {
        this.callback = callback;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged((T) this);
        }
    }
}
