package com.iafenvoy.jupiter.malilib.gui.interfaces;

import com.iafenvoy.jupiter.malilib.config.IConfigBase;

public interface IConfigInfoProvider {
    /**
     * Get the mouse-over hover info tooltip for the given config
     *
     */
    String getHoverInfo(IConfigBase config);
}
