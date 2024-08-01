package com.iafenvoy.jupiter.malilib.config;

import com.google.gson.JsonElement;
import com.iafenvoy.jupiter.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;

public interface IConfigBase {
    /**
     * Returns the type of this config. Used by the config GUI to determine what kind of control
     * to use for this config.
     *
     * @return the type of this config
     */
    ConfigType getType();

    /**
     * Returns the config name to display in the config GUIs
     *
     * @return the name of this config
     */
    String getNameKey();

    /**
     * Returns the comment displayed when hovering over the config name in the config GUI.
     * Newlines can be added with "\n". Can be null if there is no comment for this config.
     *
     * @return the comment, or null if no comment has been set
     */
    @Nullable
    String getComment();

    /**
     * Returns the "pretty name" for this config.
     * This is used in the possible toggle messages.
     *
     * @return
     */
    default String getPrettyName() {
        return StringUtils.translate(this.getNameKey());
    }

    /**
     * Returns the display name used for this config in the config GUIs
     *
     * @return
     */
    default String getConfigGuiDisplayName() {
        return this.getPrettyName();
    }

    /**
     * Set the value of this config option from a JSON element (is possible)
     *
     * @param element
     */
    void setValueFromJsonElement(JsonElement element);

    /**
     * Return the value of this config option as a JSON element, for saving into a config file.
     *
     * @return
     */
    JsonElement getAsJsonElement();

    IConfigBase copy();
}
