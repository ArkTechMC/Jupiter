package com.iafenvoy.jupiter.malilib.gui.interfaces;

public interface ISliderCallback {
    /**
     * Maximum number of values/steps the underlying data can have.
     * Return Integer.MAX_VALUE for unlimited/non-specified, like double data type ranges.
     *
     */
    int getMaxSteps();

    /**
     * Returns the relative value (within the min - max range)
     *
     */
    double getValueRelative();

    /**
     * Sets the value
     */
    void setValueRelative(double relativeValue);

    /**
     * Returns the formatted display string for the current value
     *
     */
    String getFormattedDisplayValue();
}
