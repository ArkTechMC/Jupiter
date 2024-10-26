package com.iafenvoy.jupiter.config.config;

import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ConfigBase<T> implements IConfigBase<T> {
    protected final String nameKey;
    protected String jsonKey;
    @Nullable
    protected String commentKey;
    private final List<Consumer<T>> callbacks = new ArrayList<>();

    public ConfigBase(String nameKey) {
        this.nameKey = this.jsonKey = nameKey;
    }

    public ConfigBase<T> comment(@Nullable String commentKey) {
        this.commentKey = commentKey;
        return this;
    }

    public ConfigBase<T> json(String jsonKey) {
        this.jsonKey = jsonKey;
        return this;
    }

    public ConfigBase<T> callback(Consumer<T> callback) {
        this.callbacks.add(callback);
        return this;
    }

    @Override
    public void setValue(T value) {
        this.callbacks.forEach(x -> x.accept(value));
    }

    @Override
    public String getNameKey() {
        return this.nameKey;
    }

    @Override
    public @Nullable String getComment() {
        return this.commentKey;
    }

    @Override
    public String getJsonKey() {
        return this.jsonKey;
    }
}
