package com.iafenvoy.jupiter.malilib.gui.widgets;

import com.iafenvoy.jupiter.malilib.config.IConfigBase;
import com.iafenvoy.jupiter.malilib.config.IConfigResettable;
import com.iafenvoy.jupiter.malilib.gui.GuiConfigsBase;
import com.iafenvoy.jupiter.malilib.gui.LeftRight;
import com.iafenvoy.jupiter.malilib.gui.MaLiLibIcons;
import com.iafenvoy.jupiter.malilib.util.AlphaNumComparator;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class WidgetListConfigOptions extends WidgetListConfigOptionsBase<GuiConfigsBase.ConfigOptionWrapper, WidgetConfigOption> {
    protected final GuiConfigsBase parent;
    protected final WidgetSearchBarConfigs widgetSearchConfigs;

    public WidgetListConfigOptions(int x, int y, int width, int height, int configWidth, float zLevel, boolean useKeybindSearch, GuiConfigsBase parent) {
        super(x, y, width, height, configWidth);
        this.parent = parent;

        if (useKeybindSearch) {
            this.widgetSearchConfigs = new WidgetSearchBarConfigs(x + 2, y + 4, width - 14, 20, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.widgetSearchBar = this.widgetSearchConfigs;
            this.browserEntriesOffsetY = 23;
        } else {
            this.widgetSearchConfigs = null;
            this.widgetSearchBar = new WidgetSearchBar(x + 2, y + 4, width - 14, 14, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.browserEntriesOffsetY = 17;
        }
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.widgetSearchBar.setWidth(width - 14);
    }

    @Override
    protected Collection<GuiConfigsBase.ConfigOptionWrapper> getAllEntries() {
        return this.parent.getConfigs();
    }

    @Override
    protected void reCreateListEntryWidgets() {
        this.maxLabelWidth = this.getMaxNameLengthWrapped(this.listContents);
        super.reCreateListEntryWidgets();
    }

    @Override
    protected List<String> getEntryStringsForFilter(GuiConfigsBase.ConfigOptionWrapper entry) {
        IConfigBase config = entry.getConfig();
        if (config != null) {
            ArrayList<String> list = new ArrayList<>();
            String name = config.getNameKey();
            String translated = config.getConfigGuiDisplayName();
            list.add(name.toLowerCase());
            if (!name.equals(translated))
                list.add(translated.toLowerCase());
            if (config instanceof IConfigResettable resettable && resettable.isModified())
                list.add("modified");
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    protected void addFilteredContents(Collection<GuiConfigsBase.ConfigOptionWrapper> entries) {
        if (this.widgetSearchConfigs != null) {
            String filterText = this.widgetSearchConfigs.getFilter();
            for (GuiConfigsBase.ConfigOptionWrapper entry : entries)
                if (filterText.isEmpty() || this.entryMatchesFilter(entry, filterText))
                    this.listContents.add(entry);
        } else
            super.addFilteredContents(entries);
    }

    @Override
    protected Comparator<GuiConfigsBase.ConfigOptionWrapper> getComparator() {
        return new ConfigComparator();
    }

    @Override
    protected WidgetConfigOption createListEntryWidget(int x, int y, int listIndex, boolean isOdd, GuiConfigsBase.ConfigOptionWrapper wrapper) {
        return new WidgetConfigOption(x, y, this.browserEntryWidth, this.browserEntryHeight, this.maxLabelWidth, this.configWidth, wrapper, listIndex, this.parent, this);
    }

    public int getMaxNameLengthWrapped(List<GuiConfigsBase.ConfigOptionWrapper> wrappers) {
        int width = 0;
        for (GuiConfigsBase.ConfigOptionWrapper wrapper : wrappers)
            if (wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG)
                width = Math.max(width, this.getStringWidth(wrapper.getConfig().getConfigGuiDisplayName()));
        return width;
    }

    protected static class ConfigComparator extends AlphaNumComparator implements Comparator<GuiConfigsBase.ConfigOptionWrapper> {
        @Override
        public int compare(GuiConfigsBase.ConfigOptionWrapper config1, GuiConfigsBase.ConfigOptionWrapper config2) {
            return this.compare(config1.getConfig().getNameKey(), config2.getConfig().getNameKey());
        }
    }
}
