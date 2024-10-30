package com.iafenvoy.jupiter.render.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public interface IJupiterScreen {
    int ITEM_PER_SCROLL = 2;
    int ITEM_HEIGHT = 20;
    int ITEM_SEP = 5;
}
