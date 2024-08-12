package com.iafenvoy.jupiter.malilib.interfaces;

public interface IStringConsumerFeedback {
    /**
     * @return true if the operation succeeded, false if there was some kind of an error
     */
    boolean setString(String string);
}
