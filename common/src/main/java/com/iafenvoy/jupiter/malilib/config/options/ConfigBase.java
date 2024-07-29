package com.iafenvoy.jupiter.malilib.config.options;

import com.iafenvoy.jupiter.malilib.config.ConfigType;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigNotifiable;
import com.iafenvoy.jupiter.malilib.config.IConfigResettable;
import com.iafenvoy.jupiter.malilib.interfaces.IValueChangeCallback;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigBase<T extends IConfigBase> implements IConfigBase, IConfigResettable, IConfigNotifiable<T> {
    private final ConfigType type;
    private final String nameKey;
    private final String prettyNameKey;
    private String commentKey;
    @Nullable
    private IValueChangeCallback<T> callback;

    public ConfigBase(ConfigType type, String nameKey, String commentKey) {
        this(type, nameKey, commentKey, nameKey);
    }

    public ConfigBase(ConfigType type, String nameKey, String commentKey, String prettyNameKey) {
        this.type = type;
        this.nameKey = nameKey;
        this.prettyNameKey = prettyNameKey;
        this.commentKey = commentKey;
    }

    @Override
    public ConfigType getType() {
        return this.type;
    }

    @Override
    public String getNameKey() {
        return this.nameKey;
    }

    public String getPrettyNameKey() {
        return this.prettyNameKey;
    }

    @Override
    public String getPrettyName() {
        return StringUtils.translate(this.prettyNameKey);
    }

    public String getCommentKey() {
        return this.commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

    @Override
    @Nullable
    public String getComment() {
        return StringUtils.getTranslatedOrFallback("config.comment." + this.getNameKey().toLowerCase(), StringUtils.translate(this.commentKey));
    }

    @Override
    public IConfigBase setValueChangeCallback(IValueChangeCallback<T> callback) {
        this.callback = callback;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onValueChanged() {
        if (this.callback != null) {
            this.callback.onValueChanged((T) this);
        }
    }
}
