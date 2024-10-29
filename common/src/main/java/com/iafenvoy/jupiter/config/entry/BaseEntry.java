package com.iafenvoy.jupiter.config.entry;

import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseEntry<T> implements IConfigEntry<T> {
    protected final String nameKey;
    protected String jsonKey;
    @Nullable
    protected String commentKey;
    protected final T defaultValue;
    protected T value;
    private final List<Consumer<T>> callbacks = new ArrayList<>();

    public BaseEntry(String nameKey, T defaultValue) {
        this.nameKey = this.jsonKey = nameKey;
        this.defaultValue = defaultValue;
        this.reset();
    }

    public BaseEntry<T> comment(@Nullable String commentKey) {
        this.commentKey = commentKey;
        return this;
    }

    public BaseEntry<T> json(String jsonKey) {
        this.jsonKey = jsonKey;
        return this;
    }

    public BaseEntry<T> callback(Consumer<T> callback) {
        this.callbacks.add(callback);
        return this;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
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

    @Override
    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void reset() {
        this.value = this.defaultValue;
    }
}
