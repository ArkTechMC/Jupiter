package com.iafenvoy.jupiter.malilib.util;

import com.iafenvoy.jupiter.malilib.gui.Message.MessageType;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IDirectoryNavigator;
import com.iafenvoy.jupiter.malilib.interfaces.IStringConsumerFeedback;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class DirectoryCreator implements IStringConsumerFeedback {
    protected final File dir;
    @Nullable
    protected final IDirectoryNavigator navigator;

    public DirectoryCreator(File dir, @Nullable IDirectoryNavigator navigator) {
        this.dir = dir;
        this.navigator = navigator;
    }

    @Override
    public boolean setString(String string) {
        if (string.isEmpty()) {
            InfoUtils.showGuiOrActionBarMessage(MessageType.ERROR, "malilib.error.invalid_directory", string);
            return false;
        }
        File file = new File(this.dir, string);
        if (file.exists()) {
            InfoUtils.showGuiOrActionBarMessage(MessageType.ERROR, "malilib.error.file_or_directory_already_exists", file.getAbsolutePath());
            return false;
        }
        if (!file.mkdirs()) {
            InfoUtils.showGuiOrActionBarMessage(MessageType.ERROR, "malilib.error.failed_to_create_directory", file.getAbsolutePath());
            return false;
        }
        if (this.navigator != null)
            this.navigator.switchToDirectory(file);
        InfoUtils.showGuiOrActionBarMessage(MessageType.SUCCESS, "malilib.message.directory_created", string);
        return true;
    }
}
