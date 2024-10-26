package com.iafenvoy.jupiter.config.widget;

import com.iafenvoy.jupiter.config.interfaces.IConfigBase;
import net.minecraft.client.gui.widget.ClickableWidget;

public interface IWidgetBuilder<T> {
    ClickableWidget build(int x, int y, int width, int height, IConfigBase<T> parent);
}
