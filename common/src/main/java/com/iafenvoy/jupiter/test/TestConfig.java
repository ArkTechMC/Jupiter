package com.iafenvoy.jupiter.test;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.api.ConfigData;
import com.iafenvoy.jupiter.malilib.config.options.ConfigBoolean;
import com.iafenvoy.jupiter.malilib.config.options.ConfigColor;
import com.iafenvoy.jupiter.malilib.config.options.ConfigInteger;
import com.iafenvoy.jupiter.malilib.config.options.ConfigStringList;

public class TestConfig extends ConfigData {
    public static final TestConfig INSTANCE = new TestConfig();

    public TestConfig() {
        super(Jupiter.MOD_ID, "jupiter.test", "./config/jupiter.json");
    }

    @Override
    public void init() {
        this.createTab(Jupiter.MOD_ID, "jupiter.tab1")
                .addConfig(new ConfigBoolean("this is boolean", false, ""))
                .addConfig(new ConfigInteger("this is int", 0, ""))
                .addConfig(new ConfigInteger("this is int with range", 0, -10, 10, ""))
                .addConfig(new ConfigStringList("this is string list", ImmutableList.of(), ""))
                .addConfig(new ConfigColor("this is color", "888888", ""));
        this.createTab(Jupiter.MOD_ID, "jupiter.tab2");
    }
}
