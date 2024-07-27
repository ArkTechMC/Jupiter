package com.iafenvoy.jupiter.malilib.interfaces;

import com.iafenvoy.jupiter.malilib.util.PositionUtils;

public interface ICoordinateValueModifier {
    /**
     * Modifies the existing value by the given amount
     *
     * @param type
     * @param amount
     * @return
     */
    boolean modifyValue(PositionUtils.CoordinateType type, int amount);

    /**
     * Sets the coordinate indicated by <b>type</b> to the value parsed from the string <b>newValue</b>
     *
     * @param type
     * @param newValue
     * @return
     */
    boolean setValueFromString(PositionUtils.CoordinateType type, String newValue);
}
