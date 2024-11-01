package com.iafenvoy.jupiter.test;

import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.config.container.FileConfigContainer;
import com.iafenvoy.jupiter.config.entry.*;
import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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
                .add(new StringEntry("this is string", ""))
                .add(new ListStringEntry("this is string list", List.of("1", "2", "3", "4", "5")))
                .add(new MapStringEntry("this is string map", Map.of("1", "1", "2", "2")))
                .add(new SeparatorEntry())
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

    private enum OptionsExample implements IConfigEnumEntry {
        FIRST, SECOND, THIRD;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public @NotNull IConfigEnumEntry getByName(String name) {
            return valueOf(name);
        }

        @Override
        public IConfigEnumEntry cycle(boolean clockWise) {
            return values()[(this.ordinal() + (clockWise ? 1 : -1)) % values().length];
        }
    }
}
