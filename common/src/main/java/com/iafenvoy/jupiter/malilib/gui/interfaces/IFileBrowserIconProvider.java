package com.iafenvoy.jupiter.malilib.gui.interfaces;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface IFileBrowserIconProvider {
    IGuiIcon getIconRoot();

    IGuiIcon getIconUp();

    IGuiIcon getIconCreateDirectory();

    IGuiIcon getIconSearch();

    IGuiIcon getIconDirectory();

    @Nullable
    IGuiIcon getIconForFile(File file);
}
