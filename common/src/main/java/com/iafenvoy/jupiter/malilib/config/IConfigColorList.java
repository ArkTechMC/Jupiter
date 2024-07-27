package com.iafenvoy.jupiter.malilib.config;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.malilib.util.Color4f;

import java.util.List;

public interface IConfigColorList extends IConfigBase {
    List<Color4f> getColors();

    void setColors(List<Color4f> colors);

    ImmutableList<Color4f> getDefaultColors();
}
