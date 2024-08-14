package com.iafenvoy.jupiter.test;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.FileConfigContainer;
import com.iafenvoy.jupiter.malilib.config.IConfigOptionListEntry;
import com.iafenvoy.jupiter.malilib.config.options.*;
import net.minecraft.util.Identifier;

public class TestConfig extends FileConfigContainer {
    public static final TestConfig INSTANCE = new TestConfig();

    public TestConfig() {
        super(new Identifier(Jupiter.MOD_ID, "test"), "jupiter.test", "./config/jupiter.json");
    }

    @Override
    public void init() {
        this.createTab("tab1", "jupiter.tab1")
                .addConfig(new ConfigBoolean("this is boolean", false))
                .addConfig(new ConfigInteger("this is int", 0))
                .addConfig(new ConfigInteger("this is int with range", 0, -10, 10))
                .addConfig(new ConfigStringList("this is string list", ImmutableList.of()))
                .addConfig(new ConfigColor("this is color", "888888"));
        this.createTab("tab2", "jupiter.tab2")
                .addConfig(new ConfigOptionList("this is an option list", OptionsExample.FIRST));
        this.createTab("tab3", "jupiter.tab3");
        this.createTab("tab4", "jupiter.tab4");
        this.createTab("tab5", "jupiter.tab5");
        this.createTab("tab6", "jupiter.tab6");
        this.createTab("tab7", "jupiter.tab7");
        this.createTab("tab8", "jupiter.tab8");
        this.createTab("tab9", "jupiter.tab9");
    }

    private enum OptionsExample implements IConfigOptionListEntry {
        FIRST, SECOND, THIRD;

        @Override
        public String getStringValue() {
            return this.name();
        }

        @Override
        public String getDisplayName() {
            return this.name();
        }

        @Override
        public IConfigOptionListEntry cycle(boolean forward) {
            return values()[(this.ordinal() + (forward ? 1 : -1)) % values().length];
        }

        @Override
        public IConfigOptionListEntry fromString(String value) {
            return valueOf(value);
        }
    }
}
