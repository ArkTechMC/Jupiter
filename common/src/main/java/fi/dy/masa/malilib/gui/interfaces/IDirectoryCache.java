package fi.dy.masa.malilib.gui.interfaces;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface IDirectoryCache {
    @Nullable
    File getCurrentDirectoryForContext(String context);

    void setCurrentDirectoryForContext(String context, File dir);
}
