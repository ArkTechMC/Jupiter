package com.iafenvoy.jupiter.malilib.gui.interfaces;

import com.iafenvoy.jupiter.malilib.gui.Message.MessageType;

public interface IMessageConsumer {
    void addMessage(MessageType type, String messageKey, Object... args);

    void addMessage(MessageType type, int lifeTime, String messageKey, Object... args);
}
