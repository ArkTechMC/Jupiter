package com.iafenvoy.jupiter.render.widget;

import com.iafenvoy.jupiter.interfaces.IConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface IWidgetBuilder<T> {
    Supplier<MinecraftClient> CLIENT = MinecraftClient::getInstance;

    @Nullable
    ClickableWidget build(int x, int y, int width, int height, IConfigBase<T> parent);
}
