package com.iafenvoy.jupiter.malilib.interfaces;

public interface IStringRetriever<T> {
    /**
     * Returns a string representation of the given value.
     * This may be different than just calling toString().
     *
     */
    String getStringValue(T entry);
}
