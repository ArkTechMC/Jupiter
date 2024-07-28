package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.Jupiter;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class FileConfigContainer extends AbstractConfigContainer {
    protected final String path;

    public FileConfigContainer(Identifier id, String titleNameKey, String path) {
        super(id, titleNameKey);
        this.path = path;
    }

    @Override
    public void load() {
        try {
            this.deserialize(FileUtils.readFileToString(new File(this.path), StandardCharsets.UTF_8));
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to load config: {}", this.path, e);
        }
    }

    @Override
    public void save() {
        try {
            FileUtils.write(new File(this.path), this.serialize(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Jupiter.LOGGER.error("Failed to load config: {}", this.path, e);
        }
    }
}
