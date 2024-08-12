package com.iafenvoy.jupiter.malilib.util;

import com.iafenvoy.jupiter.malilib.gui.GuiBase;
import com.iafenvoy.jupiter.malilib.gui.Message.MessageType;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IMessageConsumer;
import com.iafenvoy.jupiter.malilib.interfaces.IStringConsumer;
import com.iafenvoy.jupiter.malilib.render.MessageRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class InfoUtils {
    public static final IStringConsumer INFO_MESSAGE_CONSUMER = new InfoMessageConsumer();
    private static final MessageRenderer IN_GAME_MESSAGES = new MessageRenderer(0xA0000000, 0).setBackgroundStyle(true, false).setCentered(true, false).setExpandUp(true);

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open.
     *
     */
    public static void showGuiMessage(MessageType type, String translationKey, Object... args) {
        showGuiMessage(type, 5000, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open.
     *
     */
    public static void showGuiMessage(MessageType type, int lifeTime, String translationKey, Object... args) {
        if (MinecraftClient.getInstance().currentScreen instanceof IMessageConsumer consumer)
            consumer.addMessage(type, lifeTime, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open. Otherwise prints the message to the action bar.
     *
     */
    public static void showGuiOrActionBarMessage(MessageType type, String translationKey, Object... args) {
        showGuiOrActionBarMessage(type, 5000, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open. Otherwise prints the message to the action bar.
     *
     */
    public static void showGuiOrActionBarMessage(MessageType type, int lifeTime, String translationKey, Object... args) {
        if (MinecraftClient.getInstance().currentScreen instanceof IMessageConsumer consumer)
            consumer.addMessage(type, lifeTime, translationKey, args);
        else {
            String msg = type.getFormatting() + StringUtils.translate(translationKey, args) + GuiBase.TXT_RST;
            printActionbarMessage(msg);
        }
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open. Otherwise adds the message to the in-game message handler.
     *
     */
    public static void showGuiOrInGameMessage(MessageType type, String translationKey, Object... args) {
        showGuiOrInGameMessage(type, 5000, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open. Otherwise adds the message to the in-game message handler.
     *
     */
    public static void showGuiOrInGameMessage(MessageType type, int lifeTime, String translationKey, Object... args) {
        if (MinecraftClient.getInstance().currentScreen instanceof IMessageConsumer consumer)
            consumer.addMessage(type, lifeTime, translationKey, args);
        else
            showInGameMessage(type, lifeTime, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open.
     * Also shows the message in the in-game message box.
     *
     */
    public static void showGuiAndInGameMessage(MessageType type, String translationKey, Object... args) {
        showGuiAndInGameMessage(type, 5000, translationKey, args);
    }

    /**
     * Adds the message to the current GUI's message handler, if there is currently
     * an IMessageConsumer GUI open.
     * Also shows the message in the in-game message box.
     *
     */
    public static void showGuiAndInGameMessage(MessageType type, int lifeTime, String translationKey, Object... args) {
        showGuiMessage(type, lifeTime, translationKey, args);
        showInGameMessage(type, lifeTime, translationKey, args);
    }

    public static void printActionbarMessage(String key, Object... args) {
        sendVanillaMessage(Text.translatable(key, args));
    }

    /**
     * Adds the message to the in-game message handler
     *
     */
    public static void showInGameMessage(MessageType type, String translationKey, Object... args) {
        showInGameMessage(type, 5000, translationKey, args);
    }

    /**
     * Adds the message to the in-game message handler
     *
     */
    public static void showInGameMessage(MessageType type, int lifeTime, String translationKey, Object... args) {
        IN_GAME_MESSAGES.addMessage(type, lifeTime, translationKey, args);
    }

    public static void sendVanillaMessage(MutableText message) {
        World world = MinecraftClient.getInstance().world;

        if (world != null) {
            MinecraftClient.getInstance().inGameHud.setOverlayMessage(message, false);
        }
    }

    public static class InfoMessageConsumer implements IStringConsumer {
        @Override
        public void setString(String string) {
            printActionbarMessage(string);
        }
    }
}
