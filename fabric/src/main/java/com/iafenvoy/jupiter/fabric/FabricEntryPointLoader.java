package com.iafenvoy.jupiter.fabric;

import com.iafenvoy.jupiter.EntryPointLoader;
import com.iafenvoy.jupiter.Jupiter;
import com.iafenvoy.jupiter.api.JupiterConfigEntry;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class FabricEntryPointLoader extends EntryPointLoader {
    public static final FabricEntryPointLoader INSTANCE = new FabricEntryPointLoader();

    @Override
    protected List<JupiterConfigEntry> loadEntries() {
        return FabricLoader.getInstance().getEntrypoints(Jupiter.MOD_ID, JupiterConfigEntry.class);
    }
}
