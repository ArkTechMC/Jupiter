package com.iafenvoy.jupiter.test;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.container.FileConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.malilib.config.IConfigOptionListEntry;
import net.minecraft.util.Identifier;

public class TestConfig extends FileConfigContainer {
    public static final TestConfig INSTANCE = new TestConfig();

    public TestConfig() {
        super(new Identifier(Jupiter.MOD_ID, "test"), "jupiter.test", "./config/jupiter.json");
    }

    @Override
    public void init() {
        this.createTab("tab1", "jupiter.tab1")
                .add(new BooleanEntry("this is boolean", false))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int with range", 0, -10, 10))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0))
                .add(new IntegerEntry("this is int", 0));
//                .add(new ListStringEntry("this is string list", ImmutableList.of()));
        this.createTab("tab2", "jupiter.tab2");
//                .add(new ConfigOptionList("this is an option list", OptionsExample.FIRST));
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
