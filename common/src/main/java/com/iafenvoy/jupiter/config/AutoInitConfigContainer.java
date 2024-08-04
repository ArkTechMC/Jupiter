package com.iafenvoy.jupiter.config;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AutoInitConfigContainer extends FileConfigContainer {
    public AutoInitConfigContainer(Identifier id, String titleNameKey, String path) {
        super(id, titleNameKey, path);
    }

    @Override
    public void init() {
        for (Field field : this.getClass().getFields())
            if (AutoInitConfigCategoryBase.class.isAssignableFrom(field.getType()))
                try {
                    this.configTabs.add(((AutoInitConfigCategoryBase) field.get(this)).getCategory());
                } catch (Exception e) {
                    Jupiter.LOGGER.error("Failed to auto init category {}", field.getName(), e);
                }
    }

    public static class AutoInitConfigCategoryBase {
        private final ConfigCategory category;
        private boolean loaded = false;

        public AutoInitConfigCategoryBase(String id, String translateKey) {
            this.category = new ConfigCategory(id, translateKey, new ArrayList<>());
        }

        public ConfigCategory getCategory() {
            if (!this.loaded) {
                this.loaded = true;
                for (Field field : this.getClass().getFields())
                    if (IConfigBase.class.isAssignableFrom(field.getType()))
                        try {
                            this.category.addConfig((IConfigBase) field.get(this));
                        } catch (Exception e) {
                            Jupiter.LOGGER.error("Failed to auto init config key {}", field.getName(), e);
                        }
            }
            return this.category;
        }
    }
}
