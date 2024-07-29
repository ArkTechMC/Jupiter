package com.iafenvoy.jupiter.malilib.gui;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonBase;
import com.iafenvoy.jupiter.malilib.gui.button.ButtonGeneric;
import com.iafenvoy.jupiter.malilib.gui.button.IButtonActionListener;
import com.iafenvoy.jupiter.malilib.gui.interfaces.IStringListConsumer;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetListStringSelection;
import com.iafenvoy.jupiter.malilib.gui.widgets.WidgetStringListEntry;
import com.iafenvoy.jupiter.malilib.interfaces.IStringListProvider;
import com.iafenvoy.jupiter.malilib.util.StringUtils;

import java.util.Collection;

public class GuiStringListSelection extends GuiListBase<String, WidgetStringListEntry, WidgetListStringSelection> implements IStringListProvider {
    protected final ImmutableList<String> strings;
    protected final IStringListConsumer consumer;

    public GuiStringListSelection(Collection<String> strings, IStringListConsumer consumer) {
        super(10, 30);

        this.strings = ImmutableList.copyOf(strings);
        this.consumer = consumer;
    }

    @Override
    protected int getBrowserWidth() {
        return this.width - 20;
    }

    @Override
    protected int getBrowserHeight() {
        return this.height - 60;
    }

    @Override
    public Collection<String> getStrings() {
        return this.strings;
    }

    @Override
    protected WidgetListStringSelection createListWidget(int listX, int listY) {
        return new WidgetListStringSelection(listX, listY, this.getBrowserWidth(), this.getBrowserHeight(), this);
    }

    @Override
    public void initGui() {
        super.initGui();

        int x = 12;
        int y = this.height - 32;

        x += this.createButton(x, y, -1, ButtonListener.Type.OK) + 2;
        x += this.createButton(x, y, -1, ButtonListener.Type.CANCEL) + 2;
    }

    private int createButton(int x, int y, int width, ButtonListener.Type type) {
        ButtonListener listener = new ButtonListener(type, this);
        String label = type.getDisplayName();

        if (width == -1) {
            width = this.getStringWidth(label) + 10;
        }

        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, label);
        this.addButton(button, listener);

        return width;
    }

    private record ButtonListener(GuiStringListSelection.ButtonListener.Type type,
                                  GuiStringListSelection parent) implements IButtonActionListener {

        @Override
            public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
                if (this.type == Type.OK) {
                    this.parent.consumer.consume(this.parent.getListWidget().getSelectedEntries());
                } else {
                    GuiBase.openGui(this.parent.getParent());
                }
            }

            public enum Type {
                OK("malilib.gui.button.ok"),
                CANCEL("malilib.gui.button.cancel");

                private final String translationKey;

                Type(String translationKey) {
                    this.translationKey = translationKey;
                }

                public String getDisplayName(Object... args) {
                    return StringUtils.translate(this.translationKey, args);
                }
            }
        }
}
