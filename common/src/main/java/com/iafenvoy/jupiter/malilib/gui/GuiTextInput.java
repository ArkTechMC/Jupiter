package com.iafenvoy.jupiter.malilib.gui;

import com.iafenvoy.jupiter.malilib.gui.Message.MessageType;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IMessageConsumer;
import com.iafenvoy.jupiter.malilib.interfaces.ICompletionListener;
import com.iafenvoy.jupiter.malilib.interfaces.IStringConsumer;
import com.iafenvoy.jupiter.malilib.interfaces.IStringConsumerFeedback;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public class GuiTextInput extends GuiTextInputBase implements ICompletionListener {
    protected final IStringConsumer consumer;
    protected final IStringConsumerFeedback consumerFeedback;

    public GuiTextInput(int maxTextLength, String titleKey, String defaultText, @Nullable Screen parent, IStringConsumer consumer) {
        super(maxTextLength, titleKey, defaultText, parent);

        this.consumer = consumer;
        this.consumerFeedback = null;
    }

    public GuiTextInput(int maxTextLength, String titleKey, String defaultText, @Nullable Screen parent, IStringConsumerFeedback consumer) {
        super(maxTextLength, titleKey, defaultText, parent);

        this.consumer = null;
        this.consumerFeedback = consumer;
    }

    @Override
    protected boolean applyValue(String string) {
        if (this.consumerFeedback != null) {
            return this.consumerFeedback.setString(string);
        }

        this.consumer.setString(string);
        return true;
    }

    @Override
    public void onTaskCompleted() {
        if (this.getParent() instanceof ICompletionListener) {
            ((ICompletionListener) this.getParent()).onTaskCompleted();
        }
    }

    @Override
    public void onTaskAborted() {
        if (this.getParent() instanceof ICompletionListener) {
            ((ICompletionListener) this.getParent()).onTaskAborted();
        }
    }

    @Override
    public void addMessage(MessageType type, int lifeTime, String messageKey, Object... args) {
        if (this.getParent() instanceof IMessageConsumer) {
            ((IMessageConsumer) this.getParent()).addMessage(type, lifeTime, messageKey, args);
        } else {
            super.addMessage(type, lifeTime, messageKey, args);
        }
    }
}
